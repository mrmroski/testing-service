package com.javadevs.testingservice.model;

import jakarta.persistence.*;
import lombok.*;
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
    @SequenceGenerator(name = "questionSequenceGenerator", allocationSize = 100, initialValue = 10,
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
            throw new RuntimeException("Answer with id " + other.getId() + " is already added!");
        }
    }

    public void deleteAnswer(Long answerId) {
        Answer toDelete = this.answers.stream()
                .filter(curr -> curr.getId() == answerId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Answer with id " + answerId + " wasn't added!"));
        this.answers.remove(toDelete);
    }
}
