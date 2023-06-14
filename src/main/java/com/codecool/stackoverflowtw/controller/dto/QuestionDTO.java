package com.codecool.stackoverflowtw.controller.dto;

public record QuestionDTO(int id, String title, String description, java.sql.Date created, int numberOfAnswers) {}
