package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.common.enums.TypeRank;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.Constant;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.entities.Student;
import com.demo.entities.Subject;
import com.demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectService subjectService;

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public Student findById(String id) {
        return studentRepository.findByIdAndStatus(id, AppStatus.ACTIVE);
    }

    @Override
    public List<Student> findAllByClassId(String id) {
        return studentRepository.findAllByClassId(id);
    }

    @Override
    public Page<StudentResponse> getPageMember(String email, String phone, String classId, String searchKey, String sortField, boolean ascSort, int pageNumber, int pageSize) {

        String properties = "";
        System.out.println(sortField);
        System.out.println(ascSort);
        switch (sortField) {
            case Constant.MEMBER_FIRST_NAME:
                properties = "firstName";
                break;
            case Constant.MEMBER_LAST_NAME:
                properties = "lastName";
                break;
            case Constant.MEMBER_EMAIL:
                properties = "email";
                break;
            case Constant.MEMBER_BOB:
                properties = "bob";
                break;
            case Constant.MEMBER_PHONE:
                properties = "phone";
                break;
            default:
                properties = "createdDate";
                break;
        }

        if (sortField.equals("avg_score") && !ascSort) {
            System.out.println("vao");
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
            return studentRepository.getStudentPagingDesc(email, phone, classId, "%" + searchKey + "%", pageable);
        } else if (sortField.equals("avg_score") && ascSort) {
            System.out.println("vao ASC");
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
            return studentRepository.getStudentPagingAsc(email, phone, classId, "%" + searchKey + "%", pageable);
        } else {

            Sort.Direction direction = ascSort ? Sort.Direction.ASC : Sort.Direction.DESC;
            Sort sort = Sort.by(direction, properties);

            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
            return studentRepository.getStudentPaging(email, phone, classId, "%" + searchKey + "%", pageable);

        }
    }

    @Override
    public void saveAll(List<Student> students) {
        studentRepository.saveAll(students);
    }

    @Override
    public List<Student> findAllByStatus(AppStatus active) {
        return studentRepository.findAllByStatus(active);
    }

    @Override
    public List<StudentResponse> getTop3StudentByType(TypeRank type) {
        List<Student> students = this.findAllByStatus(AppStatus.ACTIVE);
        List<StudentResponse> listStudentResponse = new ArrayList<>();
        List<String> ids = students.stream().map(Student::getId).collect(Collectors.toList());
        List<Subject> subjectsByStudentId = subjectService.findAllByListStudentId(ids);
        students.forEach(e -> {
            List<Subject> subjects = subjectsByStudentId.stream().filter(i -> i.getStudentId().equals(e.getId())).collect(Collectors.toList());
            Double totalScore = subjects.stream().mapToDouble(Subject::getScore).sum() / subjects.size();
            listStudentResponse.add(new StudentResponse(e, subjects, totalScore));
        });
        List<StudentResponse> listStudent = new ArrayList<>();
        switch (type) {
            case EXCELLENT:
                listStudent = listStudentResponse.stream()
                        .filter(e -> e.getAvgScore() > 8.5 && e.getAvgScore() <= 10)
                        .collect(Collectors.toList());
                break;
            case GOOD:
                listStudent = listStudentResponse.stream()
                        .filter(e -> e.getAvgScore() > 6.5 && e.getAvgScore() <= 8.4)
                        .collect(Collectors.toList());
                break;
            case AVERAGE:
                listStudent = listStudentResponse.stream()
                        .filter(e -> e.getAvgScore() > 5.0 && e.getAvgScore() <= 6.4)
                        .collect(Collectors.toList());
                break;
            case WEAK:
                listStudent = listStudentResponse.stream()
                        .filter(e -> e.getAvgScore() > 2.5 && e.getAvgScore() <= 4.9)
                        .collect(Collectors.toList());
                break;
            case POOR:
                listStudent = listStudentResponse.stream()
                        .filter(e -> e.getAvgScore() <= 2.5)
                        .collect(Collectors.toList());
                break;

        }
        return listStudent;


    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAllByStatus(AppStatus.ACTIVE);
    }

    @Override
    public Student getByEmail(String email) {
        return studentRepository.getByEmail(email);
    }

    @Override
    public Student getByPhoneNumber(String phoneNumber) {
        return studentRepository.getByPhoneNumber(phoneNumber);
    }
}
