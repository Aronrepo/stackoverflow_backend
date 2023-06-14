package com.codecool.stackoverflowtw.dao;

import com.codecool.stackoverflowtw.controller.dto.NewQuestionDTO;
import com.codecool.stackoverflowtw.controller.dto.QuestionDTO;
import com.codecool.stackoverflowtw.dao.model.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionsDAO {
    List<QuestionDTO> findAllQuestions();
    Optional<QuestionDTO> findOneQuestionById(int id);
    void save(NewQuestionDTO question);
    Integer findIdOfQuestionByTitle(String title);
    void sayHi();
}
