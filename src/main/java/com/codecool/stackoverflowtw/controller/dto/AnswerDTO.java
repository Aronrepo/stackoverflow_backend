package com.codecool.stackoverflowtw.controller.dto;

import java.time.LocalDateTime;

public record AnswerDTO(int answer_id, String answer, int question_id, LocalDateTime created) {
}
