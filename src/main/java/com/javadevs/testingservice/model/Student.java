package com.javadevs.testingservice.model;

import com.javadevs.testingservice.exception.ExamAlreadyAssignedException;
import com.javadevs.testingservice.exception.ExamNotFoundException;
import com.javadevs.testingservice.exception.SubjectIsAlreadyCoveredException;
import com.javadevs.testingservice.exception.SubjectWasNotCoveredException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    @SequenceGenerator(name = "studentSequenceGenerator", allocationSize = 100, initialValue = 100,
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

    @OneToMany(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Exam> exams;

    public void addSubject(Subject other) {
        boolean noneIdMatch = this.subjectsCovered.stream()
                .noneMatch(curr -> curr.getId() == other.getId());
        if (noneIdMatch) {
            this.subjectsCovered.add(other);
        } else {
            throw new SubjectIsAlreadyCoveredException(other.getId());
        }
    }

    public void deleteSubject(Subject other) {
        Subject toDelete = this.subjectsCovered.stream()
                .filter(curr -> curr.getId() == other.getId())
                .findFirst()
                .orElseThrow(() -> new SubjectWasNotCoveredException(other.getId()));
        this.subjectsCovered.remove(toDelete);
    }

    public void assignExam(Exam exam) {
        if (!exams.contains(exam)) {
            this.exams.add(exam);
            exam.setStudent(this);
        } else {
            throw new ExamAlreadyAssignedException(exam.getId());
        }
    }

    public void removeExam(Exam exam) {
        if (exams.contains(exam)) {
            this.exams.remove(exam);
            exam.setStudent(null);
        } else {
            throw new ExamNotFoundException(exam.getId());
        }
    }
}
