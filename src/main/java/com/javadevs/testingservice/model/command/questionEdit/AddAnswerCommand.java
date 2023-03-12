package com.javadevs.testingservice.model.command.questionEdit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@Builder
public class AddAnswerCommand {
    @NotNull(message = "QUESTION_ID_NOT_NULL")
    @Positive(message = "QUESTION_ID_NOT_NEGATIVE")
    private Long questionId;
    @NotEmpty(message = "ANSWER_NOT_EMPTY")
    private String answer;
    @NotNull(message = "CORRECT_NOT_EMPTY")
    private Boolean correct;
}
