package com.javadevs.testingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Answer")
@Table(name = "answers")
@Builder
@SQLDelete(sql = "UPDATE answers SET deleted = true WHERE answer_id=? and version=?")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "question_id", foreignKey = @ForeignKey(name = "question_answer_fk"))
    private Question question;

    @Version
    private long version;

    private boolean deleted;
}
