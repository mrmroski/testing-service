package com.javadevs.testingservice.model.command.studentEdit;

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
public class AddSubjectCoveredToStudentCommand {
    @NotNull(message = "STUDENT_ID_NOT_NULL")
    @Positive(message = "STUDENT_ID_NOT_NEGATIVE")
    private Long studentId;
    @NotNull(message = "SUBJECT_ID_NOT_NULL")
    @Positive(message = "SUBJECT_ID_NOT_NEGATIVE")
    private Long subjectId;
    @NotNull(message = "VERSION_NOT_NULL")
    @PositiveOrZero(message = "VERSION_NOT_NEGATIVE")
    private Long version;
}
