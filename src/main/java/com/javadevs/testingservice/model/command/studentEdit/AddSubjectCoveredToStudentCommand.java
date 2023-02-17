package com.javadevs.testingservice.model.command.studentEdit;


import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AddSubjectCoveredToStudentCommand {
    @Positive(message = "STUDENT_ID_NOT_NEGATIVE")
    private Long studentId;
    @Positive(message = "SUBJECT_ID_NOT_NEGATIVE")
    private Long subjectId;
}
