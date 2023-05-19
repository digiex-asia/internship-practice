package com.demo.controllers.helper;

import com.demo.common.enums.AppStatus;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.controllers.model.request.subject.CreateSubjectRequest;
import com.demo.controllers.model.request.subject.UpdateSubjectRequest;
import com.demo.entities.Subject;
import org.springframework.stereotype.Component;

@Component
public class SubjectHelper {
    public Subject createSubject(CreateSubjectRequest subjectRequest, String studentId) {

        Subject subject = new Subject();
        subject.setId(UniqueID.getUUID());
        subject.setStatus(AppStatus.ACTIVE);
        subject.setScore(subjectRequest.getScore());
        subject.setName(subjectRequest.getName().trim());
        subject.setNumberOfLessons(subjectRequest.getNumberOfLessons());
        subject.setStudentId(studentId);


        return subject;
    }

    public Subject updateSubject(UpdateSubjectRequest subjectRequest, Subject subject) {
        if (subjectRequest.getId() != null && !subjectRequest.getId().trim().isEmpty()) {
            subject.setId(subjectRequest.getId());

        }
        if (subjectRequest.getScore() != null) {
            subject.setScore(subjectRequest.getScore());

        }
        if (subjectRequest.getName() != null && !subjectRequest.getName().trim().isEmpty()) {

            subject.setName(subjectRequest.getName().trim());
        }
        if (subjectRequest.getNumberOfLessons() != null) {
            subject.setNumberOfLessons(subjectRequest.getNumberOfLessons());

        }

        return subject;
    }

    public Subject createSubjectIfNull(UpdateSubjectRequest subjectRequest, String studentId) {
        System.out.println(subjectRequest.getScore());
        if (subjectRequest.getScore() == null || subjectRequest.getScore() > 10 || subjectRequest.getScore() < 0) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Score must be not null and max is 10, min is 0");
        }
        if (subjectRequest.getName() == null || subjectRequest.getName().length() > 45) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Name must be not null and length less 45");
        }
        Subject subject = new Subject();
        subject.setId(UniqueID.getUUID());
        subject.setStatus(AppStatus.ACTIVE);
        subject.setScore(subjectRequest.getScore());
        subject.setName(subjectRequest.getName().trim());
        subject.setNumberOfLessons(subjectRequest.getNumberOfLessons());
        subject.setStudentId(studentId);

        return subject;
    }
}

