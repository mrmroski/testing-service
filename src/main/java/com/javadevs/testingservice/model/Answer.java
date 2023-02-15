package com.javadevs.testingservice.model;

import jakarta.persistence.*;
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
    @SequenceGenerator(name = "answerSequenceGenerator", allocationSize = 100, initialValue = 10,
            sequenceName = "answer_sequence_generator")
    @Column(name = "answer_id")
    private long id;
    private String answer;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "question_id", foreignKey = @ForeignKey(name = "question_answer_fk"))
    private Question question;
}
