package com.demo.controllers.helper;

import com.demo.common.enums.AppStatus;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.controllers.model.request.CreateEducationRequest;
import com.demo.controllers.model.request.UpdateEducationRequest;
import com.demo.entities.Education;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EducationHelper {

    public Education createEducation(CreateEducationRequest createEducationRequest, String userId) {
        Education education = new Education();
        education.setId(UniqueID.getUUID());
        education.setStatus(AppStatus.ACTIVE);
        education.setMajor(createEducationRequest.getMajor());
        education.setEndDate(createEducationRequest.getEndDate());
        education.setStartDate(createEducationRequest.getStartDate());
        education.setDegree(createEducationRequest.getDegree());
        education.setUniversityCollege(createEducationRequest.getUniversityCollege());
        education.setUserId(userId);
        return education;
    }

    public Education createEducation(UpdateEducationRequest createEducationRequest, String userId) {
        Education education = new Education();
        education.setId(UniqueID.getUUID());
        education.setStatus(AppStatus.ACTIVE);
        education.setMajor(createEducationRequest.getMajor());
        education.setEndDate(createEducationRequest.getEndDate());
        education.setStartDate(createEducationRequest.getStartDate());
        education.setDegree(createEducationRequest.getDegree());
        education.setUniversityCollege(createEducationRequest.getUniversityCollege());
        education.setUserId(userId);
        return education;
    }

    public List<Education> updateEducation(List<UpdateEducationRequest> updateEducationRequests, List<Education> educations, String userId) {
        List<UpdateEducationRequest> addEducations = updateEducationRequests.stream().filter(e -> e.getId() == null || e.getId().trim().isEmpty()).collect(Collectors.toList());
        List<UpdateEducationRequest> updateEducations = updateEducationRequests.stream().filter(e -> e.getId() != null).collect(Collectors.toList());
        if (!addEducations.isEmpty()) {
            validationAdd(addEducations);
            for (UpdateEducationRequest educationRequest : addEducations) {
                if (educationRequest.getStartDate() > new Date().getTime() && educationRequest.getEndDate() > new Date().getTime() &&
                        educationRequest.getStartDate() >= educationRequest.getEndDate()) {
                    throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "StartDate or EndDate invalid");
                }
                educations.add(createEducation(educationRequest, userId));
            }
        }
        if (!updateEducations.isEmpty()) {
            updateEducations.forEach(educationRequest -> educations.forEach(education -> {
                if (education.getId().equals(educationRequest.getId())) {
                    if (AppStatus.INACTIVE.equals(educationRequest.getStatus())) {
                        education.setStatus(AppStatus.INACTIVE);
                    } else {
                        if (educationRequest.getMajor() != null && !educationRequest.getMajor().trim().isEmpty()) {
                            education.setMajor(educationRequest.getMajor());
                        }
                        if (educationRequest.getDegree() != null && !educationRequest.getDegree().trim().isEmpty()) {
                            education.setDegree(educationRequest.getDegree());
                        }
                        if (educationRequest.getUniversityCollege() != null && !educationRequest.getUniversityCollege().trim().isEmpty()) {
                            education.setUniversityCollege(educationRequest.getUniversityCollege());
                        }
                        if (educationRequest.getEndDate() != null) {
                            education.setEndDate(educationRequest.getEndDate());
                        }
                        if (educationRequest.getStartDate() != null) {
                            education.setStartDate(educationRequest.getStartDate());
                        }
                        if (education.getStartDate() > new Date().getTime() && education.getEndDate() > new Date().getTime() &&
                                education.getStartDate() >= education.getEndDate()) {
                            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "StartDate or EndDate invalid");
                        }
                    }

                }
            }));
        }
        List<Education> educationList = educations.stream().filter(subject -> AppStatus.ACTIVE.equals(subject.getStatus())).collect(Collectors.toList());
        List<String> names = educationList.stream().map(Education::getUniversityCollege).map(String::toLowerCase).distinct().collect(Collectors.toList());
        if (names.size() != educationList.size()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Education name is duplicated");
        }
        return educations;
    }

    private void validationAdd(List<UpdateEducationRequest> addEducations) {
        Optional<UpdateEducationRequest> degree = addEducations.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getDegree() == null || updateSubjectRequest.getDegree().trim().isEmpty()).findAny();
        if (degree.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Degree is null");
        }

        Optional<UpdateEducationRequest> major = addEducations.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getMajor() == null || updateSubjectRequest.getMajor().trim().isEmpty()).findAny();
        if (major.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Major is null");
        }
        Optional<UpdateEducationRequest> university = addEducations.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getUniversityCollege() == null || updateSubjectRequest.getUniversityCollege().trim().isEmpty()).findAny();
        if (university.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "university is null");
        }

        Optional<UpdateEducationRequest> endDate = addEducations.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getEndDate() == null || updateSubjectRequest.getEndDate() > new Date().getTime()).findAny();
        if (endDate.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "EndDate is null or End date doesn't > time now");
        }

        Optional<UpdateEducationRequest> startDate = addEducations.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getStartDate() == null).findAny();
        if (startDate.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "StartDate is null");
        }


    }
}
