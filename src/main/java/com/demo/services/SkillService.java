package com.demo.services;

import com.demo.entities.Skill;
import org.apache.commons.lang3.function.Failable;

import java.util.List;

public interface SkillService {


    List<Skill> findAllByNameIn(List<String> skills);

    List<Skill> findAllByIdIn(List<String> collect);
}
