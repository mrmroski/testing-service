package com.javadevs.testingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Answer")
@Table(name = "answers")
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answerSequenceGenerator")
    @SequenceGenerator(name = "answerSequenceGenerator", allocationSize = 100, initialValue = 100,
            sequenceName = "answer_sequence_generator")
    @Column(name = "answer_id")
    private long id;
    private String answer;
    private Boolean correct;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "question_id", foreignKey = @ForeignKey(name = "question_answer_fk"))
    private Question question;
}
