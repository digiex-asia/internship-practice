package com.demo.controllers.helper;

import com.demo.common.enums.AppStatus;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.controllers.model.request.CreateSubjectRequest;
import com.demo.controllers.model.request.UpdateSubjectRequest;
import com.demo.entities.Subject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Subject>  updateSubject(List<UpdateSubjectRequest> subjectRequests,List<Subject> subjects, String studentId) {

            List<UpdateSubjectRequest> addSubjects =
                    subjectRequests.stream()
                            .filter(
                                    updateSubjectRequest ->
                                            updateSubjectRequest.getId() == null
                                                    || updateSubjectRequest.getId().trim().isEmpty())
                            .collect(Collectors.toList());


            List<UpdateSubjectRequest> updateSubjects =
                    subjectRequests.stream()
                            .filter(
                                    updateSubjectRequest ->
                                            updateSubjectRequest.getId() != null )
                            .collect(Collectors.toList());

            if (!addSubjects.isEmpty()){
                validateAdd(addSubjects);
                for (UpdateSubjectRequest subjectRequest : addSubjects){
                    subjects.add(createSubject(subjectRequest, studentId));
                }
            }

            if (!updateSubjects.isEmpty()) { // 4
                updateSubjects.forEach(subjectRequest -> subjects.forEach(subject  -> {
                    if (subject.getId().equals(subjectRequest.getId())) {
                        if (AppStatus.INACTIVE.equals(subjectRequest.getStatus())){
                            subject.setStatus(AppStatus.INACTIVE);
                        }else {
                            if (subjectRequest.getName() != null && !subjectRequest.getName().trim().isEmpty()) {
                                subject.setName(subjectRequest.getName().trim());
                            }

                            if (subjectRequest.getNumberOfLessons() != null) {
                                subject.setNumberOfLessons(subjectRequest.getNumberOfLessons());
                            }
                            if (subjectRequest.getScore() != null) {
                                subject.setScore(subjectRequest.getScore());
                            }
                        }
                    }

                }));
            }

            List<Subject> subjectList = subjects.stream().filter(subject -> AppStatus.ACTIVE.equals(subject.getStatus())).collect(Collectors.toList());

            List<String> names = subjectList.stream().map(Subject::getName).map(String::toLowerCase).distinct().collect(Collectors.toList());

            if (names.size() != subjectList.size()) {
                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Name is duplicated");
            }

            if (subjectList.size() > 5) {
                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "mix is 3");
            }
            if (subjectList.size() < 3) {
                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "max is 5");
            }



        return subjects;
    }

    public Subject createSubject(UpdateSubjectRequest subjectRequest, String studentId) {

        Subject subject = new Subject();
        subject.setId(UniqueID.getUUID());
        subject.setStatus(AppStatus.ACTIVE);
        subject.setScore(subjectRequest.getScore());
        subject.setName(subjectRequest.getName().trim());
        subject.setNumberOfLessons(subjectRequest.getNumberOfLessons());
        subject.setStudentId(studentId);

        return subject;
    }

    public void validateAdd(List<UpdateSubjectRequest> addSubjects) {
        Optional<UpdateSubjectRequest> name = addSubjects.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getName() == null || updateSubjectRequest.getName().trim().isEmpty()).findAny();
        if (name.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "name is null");
        }

        Optional<UpdateSubjectRequest> score = addSubjects.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getScore() == null).findAny();
        if (score.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "score is null");
        }

        Optional<UpdateSubjectRequest> numberOfLesson = addSubjects.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getNumberOfLessons() == null).findAny();
        if (numberOfLesson.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "numberOfLesson is null");
        }


    }

}

