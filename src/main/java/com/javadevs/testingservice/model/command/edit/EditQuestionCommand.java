package com.javadevs.testingservice.model.command.edit;

import com.javadevs.testingservice.model.QuestionType;
import com.javadevs.testingservice.model.command.create.CreateAnswerCommand;
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
public class EditQuestionCommand {
    @NotEmpty(message = "QUESTION_NOT_EMPTY")
    private String question;
    @Positive(message = "SUBJECT_ID_NOT_NEGATIVE")
    private Long subjectId;
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;
    private Long version;
}
