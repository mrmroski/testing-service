package com.javadevs.testingservice.model.command.questionEdit;

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
public class DeleteAnswerCommand {
    @NotNull(message = "ANSWER_ID_NOT_NULL")
    @Positive(message = "ANSWER_ID_NOT_NEGATIVE")
    private Long answerId;
    @NotNull(message = "QUESTION_ID_NOT_NULL")
    @Positive(message = "QUESTION_ID_NOT_NEGATIVE")
    private Long questionId;
}
