package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.entities.Student;
import com.demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public Student findById(String id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Student> findAllByClassId(String id) {
        return studentRepository.findAllByClassId(id);
    }

    @Override
    public Page<StudentResponse> getPageMember(String email, String phone, String classId, String searchKey, String sortField, boolean ascSort, int pageNumber, int pageSize) {
        System.out.println(sortField);
        String properties = "";
        switch (sortField) {
            case "firstName":
                properties = "firstName";
                break;
            case "lastName":
                properties = "lastName";
                break;
            case "email":
                properties = "email";
                break;
            case "bob":
                properties = "bob";
                break;
            case "phone":
                properties = "phone";
                break;
            default:
                properties = "createdDate";
                break;
        }

        Sort.Direction direction = ascSort ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, properties);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return studentRepository.getStudentPaging(email, phone, classId, "%" + searchKey + "%", pageable);
    }


    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
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
    public Student getByEmail(String email) {
        return studentRepository.getByEmail(email);
    }

    @Override
    public Student getByPhoneNumber(String phoneNumber) {
        return studentRepository.getByPhoneNumber(phoneNumber);
    }
}
