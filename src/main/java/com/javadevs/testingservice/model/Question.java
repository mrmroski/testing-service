package com.javadevs.testingservice.model;

import com.javadevs.testingservice.exception.AnswerIsAlreadyAddedException;
import com.javadevs.testingservice.exception.AnswerWasNotAddedException;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@ToString(exclude = {"answers"})
@EqualsAndHashCode(exclude = {"answers"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Question")
@Table(name = "questions")
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionSequenceGenerator")
    @SequenceGenerator(name = "questionSequenceGenerator", allocationSize = 100, initialValue = 100,
            sequenceName = "question_sequence_generator")
    @Column(name = "question_id")
    private long id;
    private String question;
    private QuestionType questionType;

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
