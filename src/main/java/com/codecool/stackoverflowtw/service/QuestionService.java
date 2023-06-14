package com.codecool.stackoverflowtw.service;

import com.codecool.stackoverflowtw.dao.QuestionsDAO;
import com.codecool.stackoverflowtw.controller.dto.NewQuestionDTO;
import com.codecool.stackoverflowtw.controller.dto.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuestionService {

    private QuestionsDAO questionsDAO;

    @Autowired
    public QuestionService(QuestionsDAO questionsDAO) {
        this.questionsDAO = questionsDAO;
    }

    public List<QuestionDTO> getAllQuestions() {
        return questionsDAO.findAllQuestions();
        //return List.of(new QuestionDTO(1, "example title", "example desc", LocalDateTime.now()));
    }

    public QuestionDTO getQuestionById(int id) {
        return questionsDAO.getQuestionById(id);
        //questionsDAO.sayHi();
        //return new QuestionDTO(id, "example title", "example desc", LocalDateTime.now());
    }

    public boolean deleteQuestionById(int id) {
        // TODO
        return false;
    }

    public int addNewQuestion(NewQuestionDTO question) {
        questionsDAO.save(question);// TODO
        int createdId = questionsDAO.findIdOfQuestionByTitle(question.title());
        return createdId;
    }
}
