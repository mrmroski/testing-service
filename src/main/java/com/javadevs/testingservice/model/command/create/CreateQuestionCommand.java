package com.javadevs.testingservice.model.command.create;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
