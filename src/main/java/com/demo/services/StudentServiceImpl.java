package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.common.enums.Gender;
import com.demo.common.enums.TypeRank;
import com.demo.common.utilities.Constant;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.entities.Student;
import com.demo.entities.Subject;
import com.demo.repositories.StudentRepository;
import com.demo.repositories.specification.StudentSpecification;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
  private final StudentRepository studentRepository;

  private final StudentSpecification studentSpecification;

  public StudentServiceImpl(
      StudentRepository studentRepository,
      StudentSpecification studentSpecification) {
    this.studentRepository = studentRepository;
    this.studentSpecification = studentSpecification;
  }

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
  public Page<StudentResponse> getPageMember(
      String email,
      String phone,
      String classId,
      String searchKey,
      String sortField,
      boolean ascSort,
      int pageNumber,
      int pageSize) {

    String properties = "";
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
      Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
      return studentRepository.getStudentPagingDesc(
          email, phone, classId, "%" + searchKey + "%", pageable);
    } else if (sortField.equals("avg_score") && ascSort) {
      System.out.println("vao ASC");
      Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
      return studentRepository.getStudentPagingAsc(
          email, phone, classId, "%" + searchKey + "%", pageable);
    } else {

      Sort.Direction direction = ascSort ? Sort.Direction.ASC : Sort.Direction.DESC;
      Sort sort = Sort.by(direction, properties);
      Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
      return studentRepository.getStudentPaging(
          email, phone, classId, "%" + searchKey + "%", pageable);
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
    List<StudentResponse> students = studentRepository.getAllStudent(AppStatus.ACTIVE);

    switch (type) {
      case EXCELLENT:
        students =
                students.stream()
                .filter(e -> e.getAvgScore() > 8.5 && e.getAvgScore() <= 10).limit(3)
                .collect(Collectors.toList());
        break;
      case GOOD:
        students =
                students.stream()
                .filter(e -> e.getAvgScore() > 6.5 && e.getAvgScore() <= 8.4).limit(3)
                .collect(Collectors.toList());
        break;
      case AVERAGE:
        students =
                students.stream()
                .filter(e -> e.getAvgScore() > 5.0 && e.getAvgScore() <= 6.4).limit(3)
                .collect(Collectors.toList());
        break;
      case WEAK:
        students =
                students.stream()
                .filter(e -> e.getAvgScore() > 2.5 && e.getAvgScore() <= 4.9).limit(3)
                .collect(Collectors.toList());
        break;
      case POOR:
        students =
                students.stream()
                .filter(e -> e.getAvgScore() <= 2.5).limit(3)
                .collect(Collectors.toList());
        break;
    }
    return students;
  }

  @Override
  public List<Student> findAll() {
    return studentRepository.findAllByStatus(AppStatus.ACTIVE);
  }

  @Override
  public List<Student> getAllByClassIdIn(List<String> ids) {
    return studentRepository.findAllByClassIdInAndStatus(ids, AppStatus.ACTIVE);
  }

  @Override
  public Page<Student> getStudentPage(
      String classId,
      Gender gender,
      String firstName,
      String lastName,
      String email,
      Long startDate,
      Long endDate,
      String searchKey,
      String sortField,
      boolean ascSort,
      int pageNumber,
      int pageSize) {
    Specification<Student> specification =
        studentSpecification.getStudent(
            classId,  gender, firstName, lastName, email, startDate, endDate, searchKey, sortField, ascSort);
    PageRequest pageable = PageRequest.of(pageNumber - 1, pageSize);
    return studentRepository.findAll(specification, pageable);
  }

  @Override
  public List<StudentResponse> getStudents(String classId) {
    return studentRepository.getStudents(classId,AppStatus.ACTIVE);
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
