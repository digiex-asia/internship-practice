package com.demo.controllers.helper;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.controllers.model.request.CreateClassRequest;
import com.demo.entities.Class;
import org.springframework.stereotype.Component;

@Component
public class ClassHelper {
    public Class createClass(CreateClassRequest classRequest) {

        Class newClass = new Class();
        newClass.setId(UniqueID.getUUID());
        newClass.setStatus(AppStatus.ACTIVE);
        newClass.setName(classRequest.getName().trim());
        newClass.setMaxStudent(classRequest.getMaxStudent());

        return newClass;
    }
}
