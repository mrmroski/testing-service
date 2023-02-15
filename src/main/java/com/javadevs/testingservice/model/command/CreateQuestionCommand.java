package com.javadevs.testingservice.model.command;

import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.QuestionType;
import com.javadevs.testingservice.model.Subject;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "CORRECT_ANSWER_NOT_EMPTY")
    private String correctAnswer;
    @Positive(message = "SUBJECT_ID_NOT_NEGATIVE")
    private Long subjectId;
    @NotEmpty(message = "QUESTION_TYPE_NOT_EMPTY")
    private QuestionType questionType;

}
