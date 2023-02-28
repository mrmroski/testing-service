package com.javadevs.testingservice.model.command.edit;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "VERSION_NOT_NULL")
    @PositiveOrZero(message = "VERSION_NOT_NEGATIVE")
    private Long version;
}
