package com.javadevs.testingservice.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "exam_question")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE exam_question SET deleted = true WHERE exam_exam_id=? AND question_question_id=?")
@Where(clause = "deleted=false")
public class QuestionExam {
    @EmbeddedId
    private QuestionExamId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("examId")
    @JoinColumn(name = "exam_exam_id")
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("questionId")
    @JoinColumn(name = "question_question_id")
    private Question question;

    private boolean deleted;

    public QuestionExam(Exam e, Question q) {
        this.id = new QuestionExamId(e.getId(), q.getId());
        this.question = q;
        this.exam = e;
    }
}

