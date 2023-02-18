package com.javadevs.testingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Exam")
@Table(name = "exams")
@Builder
@SQLDelete(sql = "UPDATE exams SET deleted = true WHERE exam_id=? AND version=?")
@Where(clause = "deleted=false")
@ToString(exclude = {"student"})
@EqualsAndHashCode(exclude = {"student"})
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "examsSequenceGenerator")
    @SequenceGenerator(name = "examsSequenceGenerator", allocationSize = 100, initialValue = 10,
            sequenceName = "exams_sequence_generator")
    @Column(name = "exam_id")
    private long id;
    private LocalDate createdAt;
    private String description;
    @Version
    private long version;
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", foreignKey = @ForeignKey(name = "student_exam_fk"))
    private Student student;

    @ManyToMany
    @JoinTable(name = "exam_question", joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private Set<Question> questions;

    public void addQuestion(Question question) {
        if (!questions.contains(question)) {
            this.questions.add(question);
        }
    }

    public void removeQuestion(Question question) {
        if (questions.contains(question)) {
            this.questions.remove(question);
        }
    }

}