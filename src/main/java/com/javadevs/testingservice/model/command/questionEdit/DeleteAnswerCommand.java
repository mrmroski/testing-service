package com.javadevs.testingservice.model.command.questionEdit;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class DeleteAnswerCommand {
    @Positive(message = "ANSWER_ID_NOT_NEGATIVE")
    private Long answerId;
    @Positive(message = "QUESTION_ID_NOT_NEGATIVE")
    private Long questionId;
}
