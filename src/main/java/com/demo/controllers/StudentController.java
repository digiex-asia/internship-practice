package com.demo.controllers;

import com.demo.common.enums.AppStatus;
import com.demo.common.enums.TypeRank;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIResponse;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.Validator;
import com.demo.controllers.helper.StudentHelper;
import com.demo.controllers.helper.SubjectHelper;
import com.demo.controllers.model.request.student.CreateStudentRequest;
import com.demo.controllers.model.request.student.UpdateStudentRequest;
import com.demo.controllers.model.request.subject.CreateSubjectRequest;
import com.demo.controllers.model.request.subject.UpdateSubjectRequest;
import com.demo.controllers.model.response.PagingResponse;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.entities.Class;
import com.demo.entities.Student;
import com.demo.entities.Subject;
import com.demo.services.ClassService;
import com.demo.services.FileService;
import com.demo.services.StudentService;
import com.demo.services.SubjectService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import javax.validation.Valid;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping(ApiPath.STUDENT_API)
@RestController
public class StudentController extends AbstractBaseController {
    @Autowired
    StudentService studentService;
    @Autowired
    FileService fileService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    ClassService classService;
    @Autowired
    StudentHelper studentHelper;
    @Autowired
    SubjectHelper subjectHelper;

    @PostMapping()
    public ResponseEntity<RestAPIResponse> createStudent(
            @Valid @RequestBody CreateStudentRequest studentRequest
    ) {
        Validator.isDateFormat(studentRequest.getBob());
        Validator.validatePhone(studentRequest.getPhoneNumber());
        Validator.validateEmail(studentRequest.getEmail());
        Set<String> checkDuplicate = new HashSet<>();
        for (CreateSubjectRequest subject : studentRequest.getSubjects()) {
            if (!checkDuplicate.add(subject.getName().toLowerCase())) {
                throw new ApplicationException(RestAPIStatus.EXISTED, "Subject existed with student");
            }
        }
        Student studentByEmail = studentService.getByEmail(studentRequest.getEmail());
        Validator.mustNull(studentByEmail, RestAPIStatus.EXISTED, "Student existed");
        Student studentByPhone = studentService.getByPhoneNumber(studentRequest.getPhoneNumber());
        Validator.mustNull(studentByPhone, RestAPIStatus.EXISTED, "Student existed");

        Class class_ = classService.getById(studentRequest.getClassId());
        Validator.notNull(class_, RestAPIStatus.NOT_FOUND, "Class not found");

        Student newStudent = studentHelper.createStudent(studentRequest);
        List<Subject> subjects = new ArrayList<Subject>();

        studentRequest.getSubjects().forEach(e -> {
            Subject subject = subjectHelper.createSubject(e, newStudent.getId());
            subjects.add(subject);

        });
        studentService.save(newStudent);
        subjectService.saveAll(subjects);
        return responseUtil.successResponse(new StudentResponse(newStudent, subjects));
    }

    @GetMapping(path = "/dowload_template")
    public ResponseEntity<Resource> dowloadExcel() throws IOException {
        String filename = "template.csv";
        InputStreamResource file = new InputStreamResource(fileService.createTemplateStudent());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @PostMapping(path = "/import", consumes = {"multipart/form-data"})
    public ResponseEntity<Resource> uploadExcel(@RequestParam("file") MultipartFile file) throws IOException, ParseException {
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
    public ResponseEntity<RestAPIResponse> getDetail(
            @PathVariable(name = "id") String id
    ) {
        Student student = studentService.findById(id);
        Validator.notNull(student, RestAPIStatus.NOT_FOUND, "User Not Found");
        return responseUtil.successResponse(new StudentResponse(student, subjectService.findAllByStudentId(student.getId())));
    }

    @GetMapping(path = ApiPath.PAGE_STUDENT_BY_CLASS)
    public ResponseEntity<RestAPIResponse> getPages(
            @RequestParam(name = "id") String classId,
            @RequestParam(name = "asc_sort", required = false, defaultValue = "false")
            boolean ascSort,
            @RequestParam(name = "sort_field", required = false, defaultValue = "")
            String sortField,
            @RequestParam(name = "email", required = false)
            String email,
            @RequestParam(name = "phone", required = false)
            String phone,
            @RequestParam(name = "search_key", required = false, defaultValue = "")
            String searchKey,
            @RequestParam(name = "page_number", required = false, defaultValue = "1")
            int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10")
            int pageSize
    ) {
        Class class_ = classService.getById(classId);
        Validator.notNull(class_, RestAPIStatus.NOT_FOUND, "Class not found");
        validatePageSize(pageNumber, pageSize);
        Page<StudentResponse> userResponses = studentService.getPageMember(email, phone, classId, searchKey, sortField, ascSort, pageNumber, pageSize);
        return responseUtil.successResponse(new PagingResponse(userResponses));
    }

    @GetMapping(path = ApiPath.STUDENT_BY_CLASS)
    public ResponseEntity<RestAPIResponse> getListStudentByClassId(
            @RequestParam(name = "id") String id
    ) {
        Class class_ = classService.getById(id);
        Validator.notNull(class_, RestAPIStatus.NOT_FOUND, "Class Not Found");
        List<Student> students = studentService.findAllByClassId(class_.getId());
        List<String> ids = students.stream().map(Student::getId).collect(Collectors.toList());
        List<Subject> subjectsByStudentId = subjectService.findAllByListStudentId(ids);
        List<StudentResponse> listStudentByClassId = new ArrayList<>();
        students.forEach(e -> {
            List<Subject> subjects = subjectsByStudentId.stream().filter(i -> i.getStudentId().equals(e.getId())).collect(Collectors.toList());
            Double totalScore = subjects.stream().mapToDouble(Subject::getScore).sum() / subjects.size();
            listStudentByClassId.add(new StudentResponse(e, subjects, totalScore));
        });
        listStudentByClassId.sort(Comparator.comparing(StudentResponse::getAvgScore, Comparator.reverseOrder()));
        return responseUtil.successResponse(listStudentByClassId);
    }

    @GetMapping(path = ApiPath.STUDENT_TOP_3_API)
    public ResponseEntity<RestAPIResponse> getTop3Average(
            @RequestParam("type") TypeRank type
    ) {
        List<StudentResponse> listStudent = studentService.getTop3StudentByType(type);
        return responseUtil.successResponse(studentHelper.sortList(listStudent));
    }


    @PutMapping(path = ApiPath.ID)
    public ResponseEntity<RestAPIResponse> updateStudent(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody UpdateStudentRequest studentRequest
    ) {
        if (studentRequest.getPhoneNumber() != null) {
            Validator.validatePhone(studentRequest.getPhoneNumber());
        }
        if (studentRequest.getEmail() != null) {
            Validator.validateEmail(studentRequest.getEmail());
        }
        Student student = studentService.findById(id);
        Validator.notNull(student, RestAPIStatus.NOT_FOUND, "Student not found");
        Student studentSave = studentHelper.updateStudent(student, studentRequest);
        List<Subject> subjects = subjectService.findAllByStudentId(student.getId());
        System.out.println(subjects);
        if (studentRequest.getSubjects() != null) {
            Set<String> checkDuplicate = new HashSet<>();
            for (UpdateSubjectRequest subject : studentRequest.getSubjects()) {
                if (!checkDuplicate.add(subject.getName().toLowerCase())) {
                    throw new ApplicationException(RestAPIStatus.EXISTED, "Subject existed with student");
                }
                Subject checkSubject = subjects.stream().filter(sub -> sub.getName().trim().equalsIgnoreCase(subject.getName().trim())).findFirst().orElse(null);
                if (checkSubject != null) {
                    throw new ApplicationException(RestAPIStatus.EXISTED, "Subject existed with student");
                }
            }

            studentRequest.getSubjects().forEach(e -> {
                if (e.getId() != null) {
                    Subject subject = subjects.stream().filter(sub -> sub.getId().equals(e.getId())).findFirst()
                            .orElseThrow(() -> new ApplicationException(RestAPIStatus.BAD_REQUEST, "Subject not found"));
                    subjectHelper.updateSubject(e, subject);
                } else {
                    subjects.add(subjectHelper.createSubjectIfNull(e, studentSave.getId()));
                }
            });

            subjectService.saveAll(subjects);
        }
        studentService.save(studentSave);
        return responseUtil.successResponse(new StudentResponse(studentSave, subjects));
    }

    @DeleteMapping(path = ApiPath.ID)
    public ResponseEntity<RestAPIResponse> deleteStudent(
            @PathVariable(name = "id") String id
    ) {
        Student student = studentService.findById(id);
        Validator.notNull(student, RestAPIStatus.NOT_FOUND, "Student Not Found");
        student.setStatus(AppStatus.INACTIVE);
        studentService.save(student);
        List<Subject> subjectsUpdate = subjectService.findAllByStudentId(student.getId());
        subjectsUpdate.forEach(e -> {
            e.setStatus(AppStatus.INACTIVE);
        });
        subjectService.saveAll(subjectsUpdate);
        return responseUtil.successResponse("Delete OK");
    }

}
