package com.javadevs.testingservice.model.command.create;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@Builder
public class CreateQuestionOpenCommand {
    @NotEmpty(message = "QUESTION_NOT_EMPTY")
    private String question;
    @Positive(message = "SUBJECT_ID_NOT_NEGATIVE")
    private Long subjectId;
    @NotEmpty(message = "ANSWER_NOT_EMPTY")
    private String answer;
}
