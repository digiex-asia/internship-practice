package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.Constant;
import com.demo.controllers.model.response.ClassDetailsResponse;
import com.demo.controllers.model.response.ClassResponse;
import com.demo.entities.Class;
import com.demo.repositories.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    ClassRepository classRepository;

    @Override
    public Class save(Class class_) {
        return classRepository.save(class_);
    }

    @Override
    public Class getByName(String name) {
        return classRepository.getByName(name);
    }

    @Override
    public Class getById(String id) {
        return classRepository.findById(id).orElse(null);
    }

    @Override
    public Page<ClassResponse> getPageMember(String searchKey, String sortField, boolean ascSort, int pageNumber, int pageSize) {
        String properties = "";
        switch (sortField) {
            case "name":
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
        return classRepository.getUserPaging(AppStatus.ACTIVE,"%" + searchKey + "%", pageable);
    }

    @Override
    public List<Class> findAllByStatus(AppStatus status) {
        return classRepository.findAllByStatus(status);
    }

    @Override
    public Class getByIdAndStatus(String id, AppStatus active) {
        return classRepository.findByIdAndStatus(id,active);
    }

    @Override
    public Class findById(String id) {
        return classRepository.findByIdAndStatus(id,AppStatus.ACTIVE);
    }
}
