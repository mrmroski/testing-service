package com.javadevs.testingservice.model;

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
    @SequenceGenerator(name = "questionSequenceGenerator", allocationSize = 100, initialValue = 10,
            sequenceName = "question_sequence_generator")
    @Column(name = "question_id")
    private long id;
    private String question;
    private String correctAnswer;
    private QuestionType questionType;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Answer> answers;

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id", foreignKey = @ForeignKey(name = "subject_question_fk"))
    private Subject subject;

}
