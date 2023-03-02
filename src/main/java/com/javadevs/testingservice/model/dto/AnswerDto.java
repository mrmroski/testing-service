package com.javadevs.testingservice.model.dto;

import com.javadevs.testingservice.model.Answer;
import lombok.Data;

@Data
public class AnswerDto {

    private long id;
    private String answer;
    private Boolean correct;

    public AnswerDto(Answer src) {
        this.id = src.getId();
        this.answer = src.getAnswer();
        this.correct = src.getCorrect();
    }
}
