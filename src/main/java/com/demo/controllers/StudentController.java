package com.demo.controllers;

import com.demo.common.enums.AppStatus;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIResponse;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.Validator;
import com.demo.controllers.helper.StudentHelper;
import com.demo.controllers.helper.SubjectHelper;
import com.demo.controllers.model.request.class_.UpdateClassRequest;
import com.demo.controllers.model.request.student.CreateStudentRequest;
import com.demo.controllers.model.request.student.UpdateStudentRequest;
import com.demo.controllers.model.request.subject.CreateSubjectRequest;
import com.demo.controllers.model.response.ClassResponse;
import com.demo.controllers.model.response.PagingResponse;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.controllers.model.response.UserResponse;
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
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/api/student")
@RestController
public class StudentController extends AbstractBaseController {
    @Autowired
    StudentService studentService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    ClassService classService;
    @Autowired
    StudentHelper studentHelper;
    @Autowired
    SubjectHelper subjectHelper;

    @PostMapping()
    public ResponseEntity<RestAPIResponse> createStudent(@Valid @RequestBody CreateStudentRequest studentRequest) {

        Validator.validateEmail(studentRequest.getEmail());
        Student studentByEmail = studentService.getByEmail(studentRequest.getEmail());
        Validator.mustNull(studentByEmail, RestAPIStatus.EXISTED, "Student existed");
        Student studentByPhone = studentService.getByPhoneNumber(studentRequest.getPhoneNumber());
        Validator.mustNull(studentByPhone, RestAPIStatus.EXISTED, "Student existed");
        Class class_ = classService.getById(studentRequest.getClassId());
        Validator.notNull(class_, RestAPIStatus.NOT_FOUND, "Class not found");

        Student newStudent = studentHelper.createStudent(studentRequest);

        List<Subject> subjects = new ArrayList<Subject>();
        List<String> listTemp = new ArrayList<String>();
        studentRequest.getSubjects().forEach(e -> {


            if (listTemp.contains(e.getName())) {
                throw new ApplicationException(RestAPIStatus.EXISTED, "Subject existed with student");
            }
            Subject subject = subjectHelper.createSubject(e, newStudent.getId());

            subjects.add(subject);
            listTemp.add(e.getName());
        });
        studentService.save(newStudent);
        subjectService.saveAll(subjects);
        return responseUtil.successResponse(new StudentResponse(newStudent, subjects));
    }

    @GetMapping(path = ApiPath.ID)
    public ResponseEntity<RestAPIResponse> getDetail(
            @PathVariable(name = "id") String id
    ) {
        Student student = studentService.findById(id);
        Validator.notNull(student, RestAPIStatus.NOT_FOUND, "User Not Found");

        return responseUtil.successResponse(new StudentResponse(student, subjectService.findAllByStudentId(student.getId())));
    }

    @GetMapping(path = "/studentByClass" + ApiPath.ID)
    public ResponseEntity<RestAPIResponse> getListStudentByClassId(
            @PathVariable(name = "id") String id
    ) {
        Class class_ = classService.getById(id);
        Validator.notNull(class_, RestAPIStatus.NOT_FOUND, "Class Not Found");
        List<Student> students = studentService.findAllByClassId(class_.getId());
        List<StudentResponse> listStudentByClassId = new ArrayList<>();

        students.forEach(e -> {

            List<Subject> subjects = subjectService.findAllByStudentId(e.getId());
            Double totalScore = subjects.stream().mapToDouble(Subject::getScore).sum();

            Double avgScore = totalScore / subjects.size();
            if (Double.isNaN(avgScore)) {
                avgScore = 0.0;

            }

            listStudentByClassId.add(new StudentResponse(e, subjects, avgScore));
        });
        listStudentByClassId.sort(Comparator.comparing(StudentResponse::getAvgScore, Comparator.reverseOrder()));
        return responseUtil.successResponse(listStudentByClassId);
    }

    @GetMapping(path = "/getTop3Excellent")
    public ResponseEntity<RestAPIResponse> getTop3Excellent(
    ) {

        List<Student> students = studentService.findAllByStatus(AppStatus.ACTIVE);
        List<StudentResponse> listStudentResponse = new ArrayList<>();

        students.forEach(e -> {

            List<Subject> subjects = subjectService.findAllByStudentId(e.getId());
            Double totalScore = subjects.stream().mapToDouble(Subject::getScore).sum();

            Double avgScore = totalScore / subjects.size();
            if (Double.isNaN(avgScore)) {
                avgScore = 0.0;

            }

            listStudentResponse.add(new StudentResponse(e, subjects, avgScore));
        });

        List<StudentResponse> listStudent = listStudentResponse.stream()
                .filter(e -> e.getAvgScore() > 8.5 && e.getAvgScore() < 10)
                .collect(Collectors.toList());
        return responseUtil.successResponse(studentHelper.sortList(listStudent));
    }

    @GetMapping(path = "/getTop3Good")
    public ResponseEntity<RestAPIResponse> getTop3Good(
    ) {

        List<Student> students = studentService.findAllByStatus(AppStatus.ACTIVE);
        List<StudentResponse> listStudentResponse = new ArrayList<>();

        students.forEach(e -> {

            List<Subject> subjects = subjectService.findAllByStudentId(e.getId());
            Double totalScore = subjects.stream().mapToDouble(Subject::getScore).sum();

            Double avgScore = totalScore / subjects.size();
            if (Double.isNaN(avgScore)) {
                avgScore = 0.0;

            }

            listStudentResponse.add(new StudentResponse(e, subjects, avgScore));
        });

        List<StudentResponse> listStudent = listStudentResponse.stream()
                .filter(e -> e.getAvgScore() > 6.5 && e.getAvgScore() <= 8.4)
                .collect(Collectors.toList());
        return responseUtil.successResponse(studentHelper.sortList(listStudent));
    }

    @GetMapping(path = "/getTop3Average")
    public ResponseEntity<RestAPIResponse> getTop3Average(
    ) {

        List<Student> students = studentService.findAllByStatus(AppStatus.ACTIVE);
        List<StudentResponse> listStudentResponse = new ArrayList<>();

        students.forEach(e -> {

            List<Subject> subjects = subjectService.findAllByStudentId(e.getId());
            Double totalScore = subjects.stream().mapToDouble(Subject::getScore).sum();

            Double avgScore = totalScore / subjects.size();
            if (Double.isNaN(avgScore)) {
                avgScore = 0.0;

            }

            listStudentResponse.add(new StudentResponse(e, subjects, avgScore));
        });

        List<StudentResponse> listStudent = listStudentResponse.stream()
                .filter(e -> e.getAvgScore() > 5.0 && e.getAvgScore() <= 6.4)
                .collect(Collectors.toList());
        return responseUtil.successResponse(studentHelper.sortList(listStudent));
    }

    @GetMapping(path = "/getTop3Weak")
    public ResponseEntity<RestAPIResponse> getTop3Weak(
    ) {

        List<Student> students = studentService.findAllByStatus(AppStatus.ACTIVE);
        List<StudentResponse> listStudentResponse = new ArrayList<>();

        students.forEach(e -> {

            List<Subject> subjects = subjectService.findAllByStudentId(e.getId());
            Double totalScore = subjects.stream().mapToDouble(Subject::getScore).sum();

            Double avgScore = totalScore / subjects.size();
            if (Double.isNaN(avgScore)) {
                avgScore = 0.0;

            }

            listStudentResponse.add(new StudentResponse(e, subjects, avgScore));
        });

        List<StudentResponse> listStudent = listStudentResponse.stream()
                .filter(e -> e.getAvgScore() > 2.5 && e.getAvgScore() <= 4.9)
                .collect(Collectors.toList());
        return responseUtil.successResponse(studentHelper.sortList(listStudent));
    }

    @GetMapping(path = "/getTop3Poor")
    public ResponseEntity<RestAPIResponse> getTop3Poor(
    ) {

        List<Student> students = studentService.findAllByStatus(AppStatus.ACTIVE);
        List<StudentResponse> listStudentResponse = new ArrayList<>();

        students.forEach(e -> {

            List<Subject> subjects = subjectService.findAllByStudentId(e.getId());
            Double totalScore = subjects.stream().mapToDouble(Subject::getScore).sum();

            Double avgScore = totalScore / subjects.size();
            if (Double.isNaN(avgScore)) {
                avgScore = 0.0;

            }

            listStudentResponse.add(new StudentResponse(e, subjects, avgScore));
        });

        List<StudentResponse> listStudent = listStudentResponse.stream()
                .filter(e -> e.getAvgScore() < 2.5)
                .collect(Collectors.toList());
        return responseUtil.successResponse(studentHelper.sortList(listStudent));
    }

    @GetMapping(path = "/pagingStudentByClass" + ApiPath.ID)
    public ResponseEntity<RestAPIResponse> getPages(@PathVariable(name = "id") String classId,
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
        System.out.println(email);
        System.out.println(phone);
        System.out.println(classId);
        Page<StudentResponse> userResponses = studentService.getPageMember(email, phone, classId, searchKey, sortField, ascSort, pageNumber, pageSize);
        return responseUtil.successResponse(new PagingResponse(userResponses));
    }

    @PutMapping(path = ApiPath.ID)
    public ResponseEntity<RestAPIResponse> updateStudent(@PathVariable(name = "id") String id, @Valid @RequestBody UpdateStudentRequest studentRequest) {
        Student student = studentService.findById(id);
        Validator.validateEmail(studentRequest.getEmail());
        Validator.notNull(student, RestAPIStatus.NOT_FOUND, "Student not found");


        Student studentSave = studentHelper.updateStudent(student, studentRequest);
        List<Subject> subjects = new ArrayList<Subject>();

        if (studentRequest.getSubjects() == null || studentRequest.getSubjects().isEmpty()) {
            List<Subject> subjectsUpdate = subjectService.findAllByStudentId(studentSave.getId());
            subjectsUpdate.forEach(e -> {
                e.setStatus(AppStatus.INACTIVE);
            });
            subjectService.saveAll(subjectsUpdate);
        } else {

            List<String> listTemp = new ArrayList<String>();
            studentRequest.getSubjects().forEach(e -> {
                Subject subjectCheck = subjectService.findById(e.getId());
                if (subjectCheck != null) {
                    subjectHelper.updateSubject(e, studentSave.getId(), subjectCheck);
                } else {
                    listTemp.add(e.getName());
                    if (listTemp.contains(e.getName())) {
                        throw new ApplicationException(RestAPIStatus.EXISTED, "Subject existed with student");
                    }
                    subjects.add(subjectHelper.createSubjectIfNull(e, studentSave.getId()));

                }
            });

        }
        studentService.save(studentSave);
        subjectService.saveAll(subjects);

        return responseUtil.successResponse(new StudentResponse(studentSave, subjects));
    }

    @DeleteMapping(path = ApiPath.ID)
    public ResponseEntity<RestAPIResponse> deleteMember(
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
