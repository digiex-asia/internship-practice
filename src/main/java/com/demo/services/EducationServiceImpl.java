package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.entities.Education;
import com.demo.repositories.EducationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService{
    final private EducationRepository educationRepository;
    @Override
    public void saveAll(List<Education> educations) {
        educationRepository.saveAll(educations);
    }

    @Override
    public List<Education> findAllByUserId(String userId) {
        return educationRepository.findAllByUserIdAndStatus(userId, AppStatus.ACTIVE);
    }

    @Override
    public List<Education> findAllByUserIdIn(List<String> userIds) {
        return educationRepository.findAllByUserIdInAndStatus(userIds,AppStatus.ACTIVE);
    }
}
