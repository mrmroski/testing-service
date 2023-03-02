package com.javadevs.testingservice.model;

import com.javadevs.testingservice.exception.AnswerIsAlreadyAddedException;
import com.javadevs.testingservice.exception.AnswerWasNotAddedException;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Set;

@ToString(exclude = {"subject", "exams", "answers"})
@EqualsAndHashCode(exclude = {"subject", "exams", "answers"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Question")
@Table(name = "questions")
@Builder
@SQLDelete(sql = "UPDATE questions SET deleted = true WHERE question_id=? and version=?")
@Where(clause = "deleted=false")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionSequenceGenerator")
    @SequenceGenerator(name = "questionSequenceGenerator", allocationSize = 100, initialValue = 100,
            sequenceName = "question_sequence_generator")
    @Column(name = "question_id")
    private long id;
    private String question;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Answer> answers;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "subject_id")
    private Subject subject;


    @ManyToMany(mappedBy = "questions", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Exam> exams;

    private boolean deleted;

    @Version
    private long version;

    public void deleteAnswer(Long answerId) {
        Answer toDelete = this.answers.stream()
                .filter(curr -> curr.getId() == answerId)
                .findFirst()
                .orElseThrow(() -> new AnswerWasNotAddedException(answerId));
        this.answers.remove(toDelete);
    }
}
