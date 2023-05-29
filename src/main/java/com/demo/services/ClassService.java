package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.controllers.model.response.ClassDetailsResponse;
import com.demo.controllers.model.response.ClassResponse;
import com.demo.controllers.model.response.UserResponse;
import com.demo.entities.Class;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ClassService {
    Class save(Class clazz);

    Class getByName(String name);

    Page<ClassResponse> getPageMember(String searchKey, String sortField, boolean ascSort, int pageNumber, int pageSize);

    List<ClassResponse> getAllClass();

    Class getByIdAndStatus(String id, AppStatus active);

    Class getById(String id);
}
