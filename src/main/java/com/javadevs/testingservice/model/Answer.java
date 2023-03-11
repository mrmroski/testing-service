package com.javadevs.testingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(exclude = {"question"})
@ToString(exclude = {"question"})
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Answer")
@Table(name = "answers")
@Builder
@SQLDelete(sql = "UPDATE answers SET deleted = true WHERE answer_id=?")
@Where(clause = "deleted=false")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answerSequenceGenerator")
    @SequenceGenerator(name = "answerSequenceGenerator", allocationSize = 100, initialValue = 100,
            sequenceName = "answer_sequence_generator")
    @Column(name = "answer_id")
    private long id;
    private String answer;
    private Boolean correct;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "question_id",
            referencedColumnName = "question_id",
            foreignKey = @ForeignKey(name = "question_answer_fk")
    )
    private Question question;

    private boolean deleted;
}
