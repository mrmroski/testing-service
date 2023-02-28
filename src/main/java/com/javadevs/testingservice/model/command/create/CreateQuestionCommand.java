package com.javadevs.testingservice.model.command.create;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@Builder
public class CreateQuestionCommand {

    @NotEmpty(message = "QUESTION_NOT_EMPTY")
    private String question;
    @NotNull(message = "ANSWERS_NOT_NULL")
    private Set<CreateAnswerCommand> answers;
    @Positive(message = "SUBJECT_ID_NOT_NEGATIVE")
    private Long subjectId;

}
