package com.javadevs.testingservice.model.command.questionEdit;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    @NotNull(message = "VERSION_NOT_NULL")
    @PositiveOrZero(message = "VERSION_NOT_NEGATIVE")
    private Long version;
}
