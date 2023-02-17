package com.javadevs.testingservice.model.command.questionEdit;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AddAnswerCommand {
    @Positive(message = "QUESTION_ID_NOT_NEGATIVE")
    private Long questionId;
    @NotEmpty(message = "ANSWER_NOT_EMPTY")
    private String answer;
    @NotNull(message = "CORRECT_NOT_EMPTY")
    private Boolean correct;
}
