package com.codecool.stackoverflowtw.controller.dto;

public record QuestionDTO(int question_id, String title, String description, java.sql.Date created, int numberOfAnswers) {}
