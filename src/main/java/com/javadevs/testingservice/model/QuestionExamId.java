package com.javadevs.testingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionExamId implements Serializable {
    @Column(name = "exam_exam_id")
    private Long examId;
    @Column(name = "question_question_id")
    private Long questionId;
}
