package com.demo.controllers;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.RestAPIResponse;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.Validator;
import com.demo.controllers.helper.ClassHelper;
import com.demo.controllers.model.request.CreateClassRequest;
import com.demo.controllers.model.request.UpdateClassRequest;
import com.demo.controllers.model.response.ClassResponse;
import com.demo.controllers.model.response.PagingResponse;
import com.demo.entities.Class;
import com.demo.entities.Student;
import com.demo.entities.Subject;
import com.demo.services.ClassService;
import com.demo.services.StudentService;
import com.demo.services.SubjectService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiPath.CLASS_API)
@RestController
public class ClassController extends AbstractBaseController {
  private final ClassService classService;
  private final SubjectService subjectService;
  private final StudentService studentService;
  private final ClassHelper classHelper;

  public ClassController(
      ClassService classService,
      SubjectService subjectService,
      StudentService studentService,
      ClassHelper classHelper) {
    this.classService = classService;
    this.subjectService = subjectService;
    this.studentService = studentService;
    this.classHelper = classHelper;
  }

  @PostMapping()
  public ResponseEntity<RestAPIResponse> createClass(
      @Valid @RequestBody CreateClassRequest classRequest) {

    Class nameExisted = classService.getByName(classRequest.getName().trim());
    Validator.mustNull(nameExisted, RestAPIStatus.EXISTED, "Name Class existed");
    Class newClass = classHelper.createClass(classRequest);
    classService.save(newClass);
    return responseUtil.successResponse(new ClassResponse(newClass));
  }

  @GetMapping(path = ApiPath.ID)
  public ResponseEntity<RestAPIResponse> getDetail(@PathVariable(name = "id") String id) {
    Class class_ = classService.getById(id);
    Validator.notNull(class_, RestAPIStatus.NOT_FOUND, "Class Not Found");
    return responseUtil.successResponse(
        new ClassResponse(class_, studentService.findAllByClassId(class_.getId())));
  }

  @DeleteMapping(path = ApiPath.ID)
  public ResponseEntity<RestAPIResponse> deleteClass(@PathVariable(name = "id") String id) {
    Class clazz = classService.getById(id);
    Validator.notNull(clazz, RestAPIStatus.NOT_FOUND, "Class Not Found");

    clazz.setStatus(AppStatus.INACTIVE);
    classService.save(clazz);

    List<Student> students = studentService.findAllByClassId(clazz.getId());
    if (!students.isEmpty()) {
      List<String> studentIds = students.stream().map(Student::getId).collect(Collectors.toList());

      List<Subject> subjects = subjectService.getAllByStudentIdIn(studentIds);
      if (!subjects.isEmpty()) {
        subjects.forEach(subject -> subject.setStatus(AppStatus.INACTIVE));
        subjectService.saveAll(subjects);
      }
      students.forEach(student -> student.setStatus(AppStatus.INACTIVE));
      studentService.saveAll(students);
    }

    return responseUtil.successResponse("Delete OK");
  }

  @GetMapping()
  public ResponseEntity<RestAPIResponse> getPages(
      @RequestParam(name = "asc_sort", required = false, defaultValue = "false") boolean ascSort,
      @RequestParam(name = "sort_field", required = false, defaultValue = "") String sortField,
      @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
      @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
      @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize) {
    validatePageSize(pageNumber, pageSize);
    Page<ClassResponse> classResponses =
        classService.getPageMember(searchKey, sortField, ascSort, pageNumber, pageSize);
    return responseUtil.successResponse(new PagingResponse(classResponses));
  }

  @GetMapping(ApiPath.ALL)
  public ResponseEntity<RestAPIResponse> getAllClass() {

    List<ClassResponse> allClass = classService.getAllClass();
    if (!allClass.isEmpty()) {
      List<String> classIds =
          allClass.stream().map(ClassResponse::getId).collect(Collectors.toList());
      List<Student> students = studentService.getAllByClassIdIn(classIds);
      if (!students.isEmpty()) {
        allClass.forEach(
            c ->
                c.setStudents(
                    students.stream()
                        .filter(student -> student.getClassId().equals(c.getId()))
                        .collect(Collectors.toList())));
      }
    }
    return responseUtil.successResponse(allClass);
  }

  @PutMapping(path = ApiPath.ID)
  public ResponseEntity<RestAPIResponse> updateClass(
      @PathVariable(name = "id") String id, @Valid @RequestBody UpdateClassRequest updateClass) {

    Class clazz = classService.getById(id);
    Validator.notNull(clazz, RestAPIStatus.NOT_FOUND, "Class Not Found");

    if (updateClass.getName() != null
        && !updateClass.getName().trim().isEmpty()
        && !updateClass.getName().trim().equals(clazz.getName())) {

      Class className = classService.getByName(updateClass.getName().trim());
      Validator.mustNull(className, RestAPIStatus.EXISTED, "Class existed");

      clazz.setName(updateClass.getName().trim());
    }

    if (updateClass.getMaxStudent() != null
        && !clazz.getMaxStudent().equals(updateClass.getMaxStudent())) {
      clazz.setMaxStudent(updateClass.getMaxStudent());
    }
    classService.save(clazz);
    return responseUtil.successResponse(new ClassResponse(clazz));
  }
}
