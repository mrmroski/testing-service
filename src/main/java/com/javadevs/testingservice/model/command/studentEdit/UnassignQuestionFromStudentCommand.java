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
public class UnassignQuestionFromStudentCommand {
    @Positive(message = "STUDENT_ID_NOT_NEGATIVE")
    private Long studentId;
    @Positive(message = "QUESTION_ID_NOT_NEGATIVE")
    private Long questionId;

}
