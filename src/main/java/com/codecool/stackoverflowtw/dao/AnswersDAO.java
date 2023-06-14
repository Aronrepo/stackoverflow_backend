package com.codecool.stackoverflowtw.dao;

import com.codecool.stackoverflowtw.controller.dto.AnswerDTO;
import com.codecool.stackoverflowtw.controller.dto.NewAnswerDTO;

import java.util.List;

public interface AnswersDAO {
    List<AnswerDTO> findAllAnswers();
    void save(NewAnswerDTO answer);
    Integer findIdOfAnswerByTitle(String answer);
}
