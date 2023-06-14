package com.codecool.stackoverflowtw.service;

import com.codecool.stackoverflowtw.controller.dto.AnswerDTO;
import com.codecool.stackoverflowtw.controller.dto.NewAnswerDTO;
import com.codecool.stackoverflowtw.dao.AnswersDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class AnswerService {
    private AnswersDAO answersDAO;

    @Autowired
    public AnswerService(AnswersDAO answersDAO) {
        this.answersDAO = answersDAO;
    }

    public List<AnswerDTO> getAllAnswers(int id) {
        return answersDAO.findAllAnswersForQuestion(id);
        //return List.of(new AnswerDTO(1, "example", 1, LocalDateTime.now()));
    }

    public boolean deleteAnswerById(int id) {
        // TODO
        return false;
    }

    public int addNewAnswer(NewAnswerDTO answer) {
        answersDAO.save(answer);
        int createdId = answersDAO.findIdOfAnswerByTitle(answer.answer());
        return createdId;
    }
}
