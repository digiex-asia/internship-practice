package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.entities.Subject;
import com.demo.repositories.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void saveAll(List<Subject> subjects) {
        subjectRepository.saveAll(subjects);
    }

    @Override
    public List<Subject> findAllByStudentId(String id) {
        return subjectRepository.findAllByStudentId(id);
    }

    @Override
    public void deleteByStudentId(String id) {
        subjectRepository.deleteByStudentId(id);
    }

    @Override
    public Subject findById(String id) {
        return subjectRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Subject subject) {
        subjectRepository.save(subject);
    }

    @Override
    public List<Subject> getAllByStudentIdIn(List<String> ids) {
        return subjectRepository.findAllByStudentIdInAndStatus(ids, AppStatus.ACTIVE);
    }

    @Override
    public List<Subject> getAllByIdIn(List<String> ids) {
        return subjectRepository.findAllByIdInAndStatus(ids, AppStatus.ACTIVE);
    }


}
