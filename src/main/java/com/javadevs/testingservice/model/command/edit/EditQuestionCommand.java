package com.javadevs.testingservice.model.command.edit;

import com.javadevs.testingservice.model.QuestionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class EditQuestionCommand {
    private String question;
    private Long subjectId;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @NotNull(message = "VERSION_NOT_NULL")
    @PositiveOrZero(message = "VERSION_NOT_NEGATIVE")
    private Long version;
}
