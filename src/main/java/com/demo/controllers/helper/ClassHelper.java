package com.demo.controllers.helper;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.controllers.model.request.class_.CreateClassRequest;
import com.demo.entities.Class;
import org.springframework.stereotype.Component;

@Component
public class ClassHelper {
    public Class createClass(CreateClassRequest classRequest) {

        Class class_ = new Class();
        class_.setId(UniqueID.getUUID());
        class_.setStatus(AppStatus.ACTIVE);
        class_.setName(classRequest.getName().trim());
        class_.setMaxSudent(classRequest.getMax_student());

        return class_;
    }
}
