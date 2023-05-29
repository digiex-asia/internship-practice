package com.demo.controllers;

import com.demo.common.enums.AppStatus;
import com.demo.common.enums.Gender;
import com.demo.common.enums.TypeRank;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIResponse;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.Validator;
import com.demo.controllers.helper.StudentHelper;
import com.demo.controllers.helper.SubjectHelper;
import com.demo.controllers.model.request.CreateStudentRequest;
import com.demo.controllers.model.request.CreateSubjectRequest;
import com.demo.controllers.model.request.UpdateStudentRequest;
import com.demo.controllers.model.request.UpdateSubjectRequest;
import com.demo.controllers.model.response.PagingResponse;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.entities.Class;
import com.demo.entities.Student;
import com.demo.entities.Subject;
import com.demo.services.ClassService;
import com.demo.services.FileService;
import com.demo.services.StudentService;
import com.demo.services.SubjectService;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(ApiPath.STUDENT_API)
@RestController
public class StudentController extends AbstractBaseController {

  private final StudentService studentService;
  private final FileService fileService;
  private final SubjectService subjectService;
  private final ClassService classService;
  private final StudentHelper studentHelper;
  private final SubjectHelper subjectHelper;

  public StudentController(
      StudentService studentService,
      FileService fileService,
      SubjectService subjectService,
      ClassService classService,
      StudentHelper studentHelper,
      SubjectHelper subjectHelper) {
    this.studentService = studentService;
    this.fileService = fileService;
    this.subjectService = subjectService;
    this.classService = classService;
    this.studentHelper = studentHelper;
    this.subjectHelper = subjectHelper;
  }

  @PostMapping()
  public ResponseEntity<RestAPIResponse> createStudent(
      @Valid @RequestBody CreateStudentRequest studentRequest) {

    Validator.validatePhone(studentRequest.getPhoneNumber().trim());
    Validator.validateEmail(studentRequest.getEmail().trim());

    if (studentRequest.getSubjects().size() > 5 || studentRequest.getSubjects().size() < 3) {
      throw new ApplicationException(
          RestAPIStatus.EXISTED, "Subjects must be not null with size more 3 and size less 5");
    }

    List<String> names = studentRequest.getSubjects().stream().map(CreateSubjectRequest::getName).map(String::toLowerCase).distinct().collect(Collectors.toList());

    if (names.size() !=  studentRequest.getSubjects().size()) {
      throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Name is duplicated");
    }

    Class clazz = classService.getById(studentRequest.getClassId());
    Validator.notNull(clazz, RestAPIStatus.NOT_FOUND, "Class not found");

    Student studentByEmail = studentService.getByEmail(studentRequest.getEmail().trim());
    Validator.mustNull(studentByEmail, RestAPIStatus.EXISTED, "Student existed");

    Student studentByPhone =
        studentService.getByPhoneNumber(studentRequest.getPhoneNumber().trim());
    Validator.mustNull(studentByPhone, RestAPIStatus.EXISTED, "Student existed");

    Student newStudent = studentHelper.createStudent(studentRequest);

    List<Subject> subjects = new ArrayList<>();
    for (CreateSubjectRequest createSubject : studentRequest.getSubjects()) {
      subjects.add(subjectHelper.createSubject(createSubject, newStudent.getId()));
    }
    subjectService.saveAll(subjects);
    studentService.save(newStudent);
    return responseUtil.successResponse(new StudentResponse(newStudent, subjects));
  }

  @GetMapping(path = "/download-template")
  public ResponseEntity<Resource> dowloadExcel() throws IOException {
    String filename = "template.csv";
    InputStreamResource file = new InputStreamResource(fileService.createTemplateStudent());
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }

  @PostMapping(
      path = "/import",
      consumes = {"multipart/form-data"})
  public ResponseEntity<Resource> uploadExcel(@RequestParam("file") MultipartFile file)
      throws IOException, ParseException {
    String extension = FilenameUtils.getExtension(file.getOriginalFilename());
    if (!Objects.equals(extension, "csv")) {
      throw new ApplicationException(RestAPIStatus.BAD_PARAMS, "File import must be csv");
    }
    String filename = "fileResponse.csv";
    InputStreamResource fileResponse = new InputStreamResource(fileService.readFile(file));
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(fileResponse);
  }

  @GetMapping(path = "/export")
  public ResponseEntity<Resource> exportExcel() throws IOException {
    String filename = "exportFile.csv";

    InputStreamResource file = new InputStreamResource(fileService.exportExcelAllStudent());
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }

  @GetMapping(path = ApiPath.ID)
  public ResponseEntity<RestAPIResponse> getDetail(@PathVariable(name = "id") String id) {
    Student student = studentService.findById(id);
    Validator.notNull(student, RestAPIStatus.NOT_FOUND, "User Not Found");
    return responseUtil.successResponse(
        new StudentResponse(student, subjectService.findAllByStudentId(student.getId())));
  }

  @GetMapping(path = ApiPath.PAGE)
  public ResponseEntity<RestAPIResponse> getPages(
      @RequestParam(name = "id") String classId,
      @RequestParam(name = "asc_sort", required = false, defaultValue = "false") boolean ascSort,
      @RequestParam(name = "sort_field", required = false, defaultValue = "") String sortField,
      @RequestParam(name = "email", required = false) String email,
      @RequestParam(name = "gender", required = false) Gender gender,
      @RequestParam(name = "first_name", required = false) String firstName,
      @RequestParam(name = "last_name", required = false) String lastName,
      @RequestParam(name = "start_date", required = false) Long startDate,
      @RequestParam(name = "end_date", required = false) Long endDate,
      @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
      @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
      @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize) {

    validatePageSize(pageNumber, pageSize);
    Page<Student> studentPage =
        studentService.getStudentPage(
            classId,
            gender,
            firstName,
            lastName,
            email,
            startDate,
            endDate,
            searchKey,
            sortField,
            ascSort,
            pageNumber,
            pageSize);
    return responseUtil.successResponse(new PagingResponse(studentPage));
  }

  @GetMapping(path = ApiPath.LIST)
  public ResponseEntity<RestAPIResponse> getListStudent(
      @RequestParam(name = "class_id") String classId) {

    List<StudentResponse> students = studentService.getStudents(classId);

    if (!students.isEmpty()) {
      List<String> studentIds =
          students.stream().map(StudentResponse::getId).collect(Collectors.toList());
      List<Subject> subjectList = subjectService.getAllByStudentIdIn(studentIds);
      for (StudentResponse student : students) {
        student.setSubjects(
            subjectList.stream()
                .filter(subject -> student.getId().equals(subject.getId()))
                .collect(Collectors.toList()));
      }
    }
    return responseUtil.successResponse(students);
  }

  @GetMapping(path = ApiPath.STUDENT_TOP_3_API)
  public ResponseEntity<RestAPIResponse> getTop3Average(@RequestParam("type") TypeRank type) {
    return responseUtil.successResponse(studentService.getTop3StudentByType(type));
  }

  @PutMapping(path = ApiPath.ID)
  public ResponseEntity<RestAPIResponse> updateStudent(
      @PathVariable(name = "id") String id,
      @Valid @RequestBody UpdateStudentRequest studentRequest) {

    Student student = studentService.findById(id);
    Validator.notNull(student, RestAPIStatus.NOT_FOUND, "Student not found");
    student = studentHelper.updateStudent(student, studentRequest);

    List<Subject> subjects = subjectService.findAllByStudentId(student.getId());
    if (studentRequest.getSubjects() != null) {
     subjects = subjectHelper.updateSubject(studentRequest.getSubjects(), subjects,student.getId());
      subjectService.saveAll(subjects);
    }
    studentService.save(student);
    return responseUtil.successResponse(new StudentResponse(student, subjects));
  }

  @DeleteMapping(path = ApiPath.ID)
  public ResponseEntity<RestAPIResponse> deleteStudent(@PathVariable(name = "id") String id) {
    Student student = studentService.findById(id);
    Validator.notNull(student, RestAPIStatus.NOT_FOUND, "Student Not Found");
    student.setStatus(AppStatus.INACTIVE);
    studentService.save(student);

    List<Subject> subjectsUpdate = subjectService.findAllByStudentId(student.getId());
    subjectsUpdate.forEach(
        e -> {
          e.setStatus(AppStatus.INACTIVE);
        });
    subjectService.saveAll(subjectsUpdate);
    return responseUtil.successResponse("Delete OK");
  }
}
