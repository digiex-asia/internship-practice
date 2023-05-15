package com.demo.services;

import com.demo.entities.Subject;
import com.demo.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService{
    @Autowired
    SubjectRepository subjectRepository;
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


}
