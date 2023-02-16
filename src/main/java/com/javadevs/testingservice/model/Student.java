package com.javadevs.testingservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ToString(exclude = {"subjectsCovered"})
@EqualsAndHashCode(exclude = {"subjectsCovered"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Student")
@Table(name = "students")
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentSequenceGenerator")
    @SequenceGenerator(name = "studentSequenceGenerator", allocationSize = 100, initialValue = 10,
            sequenceName = "student_sequence_generator")
    @Column(name = "student_id")
    private long id;
    private String name;
    private String lastname;
    private String email;
    private LocalDate startedAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "student_subject", joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjectsCovered;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "student_questions", joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private Set<Question> questions;

    public void addSubject(Subject other) {
        boolean noneIdMatch = this.subjectsCovered.stream()
                .noneMatch(curr -> curr.getId() == other.getId());
        if (noneIdMatch) {
            this.subjectsCovered.add(other);
        } else {
            throw new RuntimeException("Subject with id " + other.getId() + " is already covered!");
        }
    }

    public void assignQuestion(Question other) {
        boolean noneIdMatch = this.questions.stream()
                .noneMatch(curr -> curr.getId() == other.getId());
        if (noneIdMatch) {
            this.questions.add(other);
        } else {
            throw new RuntimeException("Question with id " + other.getId() + " is already assigned!");
        }
    }

    public void deleteSubject(Subject other) {
        Subject toDelete = this.subjectsCovered.stream()
                .filter(curr -> curr.getId() == other.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Subject with id " + other.getId() + " wasn't covered!"));
        this.subjectsCovered.remove(toDelete);
    }

    public void unassignQuestion(Question other) {
        Question toDelete = this.questions.stream()
                .filter(curr -> curr.getId() == other.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Question with id " + other.getId() + " wasn't assigned!"));
        this.questions.remove(toDelete);
    }
}
