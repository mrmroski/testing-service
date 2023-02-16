package com.javadevs.testingservice.model.dto;

import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.QuestionType;
import com.javadevs.testingservice.model.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {

    private long id;
    private String question;
    private SubjectDto subject;
    private QuestionType questionType;
    private Set<AnswerDto> answers;
}
