package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.Constant;
import com.demo.controllers.model.response.ClassResponse;
import com.demo.entities.Class;
import com.demo.repositories.ClassRepository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ClassServiceImpl implements ClassService {
  private final ClassRepository classRepository;

  public ClassServiceImpl(ClassRepository classRepository) {
    this.classRepository = classRepository;
  }

  @Override
  public Class save(Class clazz) {
    return classRepository.save(clazz);
  }

  @Override
  public Class getByName(String name) {
    return classRepository.findFirstByNameAndStatus(name, AppStatus.ACTIVE);
  }

  @Override
  public Page<ClassResponse> getPageMember(
      String searchKey, String sortField, boolean ascSort, int pageNumber, int pageSize) {
    String properties = "";
    switch (sortField) {
      case Constant.SORT_BY_NAME:
        properties = "name";
        break;
      case "maxStudent":
        properties = "maxStudent";
        break;
      default:
        properties = "createdDate";
        break;
    }

    Sort.Direction direction = ascSort ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, properties);
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
    return classRepository.getUserPaging(AppStatus.ACTIVE, "%" + searchKey + "%", pageable);
  }

  @Override
  public List<ClassResponse> getAllClass() {
    return classRepository.getAllClass(AppStatus.ACTIVE);
  }

  @Override
  public Class getByIdAndStatus(String id, AppStatus active) {
    return classRepository.findByIdAndStatus(id, active);
  }

  @Override
  public Class getById(String id) {
    return classRepository.findByIdAndStatus(id, AppStatus.ACTIVE);
  }
}
