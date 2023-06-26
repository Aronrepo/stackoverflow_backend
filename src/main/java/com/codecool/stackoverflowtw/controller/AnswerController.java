package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.AnswerDTO;
import com.codecool.stackoverflowtw.service.AnswerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("answers")
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/{id}")
    public List<AnswerDTO> getAllAnswersByQuestion(@PathVariable int id) {
        return answerService.getAllAnswersByQuestion(id);
    }
}
