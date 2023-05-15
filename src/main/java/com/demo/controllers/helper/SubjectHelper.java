package com.demo.controllers.helper;

import com.demo.common.enums.AppStatus;
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

    public Subject updateSubject(UpdateSubjectRequest subjectRequest, String studentId, Subject subject) {
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
        if (subjectRequest.getStudentId() != null && !subjectRequest.getStudentId().trim().isEmpty()) {

            subject.setStudentId(studentId);
        }
        return subject;
    }

    public Subject createSubjectIfNull(UpdateSubjectRequest subjectRequest, String studentId) {

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

