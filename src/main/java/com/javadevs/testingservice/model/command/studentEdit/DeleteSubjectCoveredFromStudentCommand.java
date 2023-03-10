package com.javadevs.testingservice.model.command.studentEdit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@Builder
public class DeleteSubjectCoveredFromStudentCommand {
    @NotNull(message = "STUDENT_ID_NOT_NULL")
    @Positive(message = "STUDENT_ID_NOT_NEGATIVE")
    private Long studentId;
    @NotNull(message = "SUBJECT_ID_NOT_NULL")
    @Positive(message = "SUBJECT_ID_NOT_NEGATIVE")
    private Long subjectId;
}
