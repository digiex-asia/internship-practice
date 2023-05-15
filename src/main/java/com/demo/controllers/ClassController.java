package com.demo.controllers;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.RestAPIResponse;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.Validator;
import com.demo.controllers.helper.ClassHelper;
import com.demo.controllers.model.request.class_.CreateClassRequest;
import com.demo.controllers.model.request.class_.UpdateClassRequest;
import com.demo.controllers.model.response.ClassDetailsResponse;
import com.demo.controllers.model.response.ClassResponse;
import com.demo.controllers.model.response.PagingResponse;
import com.demo.entities.Class;
import com.demo.entities.Student;
import com.demo.entities.Subject;
import com.demo.services.ClassService;
import com.demo.services.StudentService;
import com.demo.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(ApiPath.CLASS_API)
@RestController
public class ClassController extends AbstractBaseController {
    @Autowired
    ClassService classService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    StudentService studentService;
    @Autowired
    ClassHelper classHelper;
    @PostMapping("")
    public ResponseEntity<RestAPIResponse> createClass(@Valid @RequestBody CreateClassRequest classRequest) {
        Class class_ = classService.getByName(classRequest.getName());
        Validator.mustNull(class_, RestAPIStatus.EXISTED, "Class existed");
        Class newClass_ = classHelper.createClass(classRequest);
        classService.save(newClass_);
        return responseUtil.successResponse(new ClassResponse(newClass_));
    }
    @GetMapping(path = ApiPath.ID)
    public ResponseEntity<RestAPIResponse> getDetail(
            @PathVariable(name = "id") String id
    ) {
        Class class_ = classService.getById(id);
        Validator.notNull(class_, RestAPIStatus.NOT_FOUND, "Class Not Found");
        return responseUtil.successResponse(new ClassResponse(class_, studentService.findAllByClassId(class_.getId())));
    }
    @DeleteMapping(path = ApiPath.ID)
    public ResponseEntity<RestAPIResponse> deleteClass(
            @PathVariable(name = "id") String id
    ) {
        Class class_ = classService.findById(id);
        Validator.notNull(class_, RestAPIStatus.NOT_FOUND, "Class Not Found");
        class_.setStatus(AppStatus.INACTIVE);
        classService.save(class_);
        List<Student> students = studentService.findAllByClassId(class_.getId());
        students.forEach(e -> {
            e.setStatus(AppStatus.INACTIVE);
            List<Subject> subjects = subjectService.findAllByStudentId(e.getId());
            subjects.forEach(i -> i.setStatus(AppStatus.INACTIVE));
            subjectService.saveAll(subjects);
        });
        studentService.saveAll(students);
        return responseUtil.successResponse("Delete OK");
    }
    @GetMapping()
    public ResponseEntity<RestAPIResponse> getPages(
            @RequestParam(name = "asc_sort", required = false, defaultValue = "false") boolean ascSort,
            @RequestParam(name = "sort_field", required = false, defaultValue = "") String sortField,
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {

        validatePageSize(pageNumber, pageSize);
        Page<ClassResponse> classResponses = classService.getPageMember(searchKey, sortField, ascSort, pageNumber, pageSize);
        return responseUtil.successResponse(new PagingResponse(classResponses));
    }
    @GetMapping(ApiPath.CLASS_API + ApiPath.ALL)
    public ResponseEntity<RestAPIResponse> getAllClass(

    ) {
        List<Class> allClass = classService.findAllByStatus(AppStatus.ACTIVE);
        List<ClassDetailsResponse> allClassDetails = new ArrayList<ClassDetailsResponse>();
        allClass.stream().forEach(e -> {
            List<Student> students = studentService.findAllByClassId(e.getId());
            allClassDetails.add(new ClassDetailsResponse(e.getId(), e.getName(), e.getMaxSudent(), e.getStatus(), students));
        });
        return responseUtil.successResponse(allClassDetails);
    }
    @PutMapping(path = ApiPath.ID)
    public ResponseEntity<RestAPIResponse> updateClass(@PathVariable(name = "id") String id, @Valid @RequestBody UpdateClassRequest classRequest) {
        Class class_ = classService.getById(id);
        Validator.notNull(class_, RestAPIStatus.NOT_FOUND, "Class Not Found");
        Class getClassName = classService.getByName(classRequest.getName());
        Validator.mustNull(getClassName, RestAPIStatus.EXISTED, "Class existed");
        if (classRequest.getName() != null && !classRequest.getName().trim().isEmpty()) {
            class_.setName(classRequest.getName().trim());
        }
        if (classRequest.getMax_student() == null || classRequest.getMax_student() == 0) {
            class_.setMaxSudent(classRequest.getMax_student());
        }
        classService.save(class_);
        return responseUtil.successResponse(new ClassResponse(class_));
    }
}
