package com.javadevs.testingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@SQLDelete(sql = "UPDATE questions SET deleted = true WHERE question_id=? and version=?")
@Where(clause = "deleted=false")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public abstract class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionSequenceGenerator")
    @SequenceGenerator(name = "questionSequenceGenerator", allocationSize = 100, initialValue = 100,
            sequenceName = "question_sequence_generator")
    @Column(name = "question_id")
    private long id;
    private String question;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToMany(mappedBy = "questions", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Exam> exams;

    private boolean deleted;

    @Version
    private long version;
}
