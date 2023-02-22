package com.javadevs.testingservice.model;

import com.javadevs.testingservice.exception.AnswerIsAlreadyAddedException;
import com.javadevs.testingservice.exception.AnswerWasNotAddedException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Set;

@ToString(exclude = {"answers"})
@EqualsAndHashCode(exclude = {"answers"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Question")
@Table(name = "questions")
@Builder
@SQLDelete(sql = "UPDATE questions SET deleted = true WHERE question_id=? AND version=?")
@Where(clause = "deleted=false")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionSequenceGenerator")
    @SequenceGenerator(name = "questionSequenceGenerator", allocationSize = 100, initialValue = 100,
            sequenceName = "question_sequence_generator")
    @Column(name = "question_id")
    private long id;
    private String question;
    private QuestionType questionType;
    private boolean deleted;
    @Version
    private long version;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<Answer> answers;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public void addAnswer(Answer other) {
        boolean noneIdMatch = this.answers.stream()
                .noneMatch(curr -> curr.getId() == other.getId());
        if (noneIdMatch) {
            this.answers.add(other);
        } else {
            throw new AnswerIsAlreadyAddedException(other.getId());
        }
    }

    public void deleteAnswer(Long answerId) {
        Answer toDelete = this.answers.stream()
                .filter(curr -> curr.getId() == answerId)
                .findFirst()
                .orElseThrow(() -> new AnswerWasNotAddedException(answerId));
        this.answers.remove(toDelete);
    }
}
