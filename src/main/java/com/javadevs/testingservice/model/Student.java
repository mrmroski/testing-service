package com.javadevs.testingservice.model;

import com.javadevs.testingservice.exception.ExamNotFoundException;
import com.javadevs.testingservice.exception.SubjectIsAlreadyCoveredException;
import com.javadevs.testingservice.exception.SubjectWasNotCoveredException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDate;
import java.util.Set;

//@ToString(exclude = {"subjects", "exams"})
//@EqualsAndHashCode(exclude = {"subjects", "exams"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Student")
@Table(name = "students")
@Builder
@SQLDelete(sql = "UPDATE students SET deleted = true WHERE student_id=? and version=?")
@Where(clause = "deleted=false")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentSequenceGenerator")
    @SequenceGenerator(name = "studentSequenceGenerator", allocationSize = 100, initialValue = 100,
            sequenceName = "student_sequence_generator")
    @Column(name = "student_id")
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private LocalDate startedAt;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects;

    @OneToMany(mappedBy = "student", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Exam> exams;

    private boolean deleted;

    @Version
    private long version;

    public void addSubject(Subject other) {
        boolean noneIdMatch = this.subjects.stream()
                .noneMatch(curr -> curr.getId() == other.getId());

        if (noneIdMatch) {
            this.subjects.add(other);
        } else {
            throw new SubjectIsAlreadyCoveredException(other.getId());
        }
    }

    public void deleteSubject(Subject other) {
        Subject found = null;
        for (Subject ss : this.getSubjects()) {
            if (ss.getId() == other.getId()) {
                found = ss;
                this.subjects.remove(ss);
            }
        }

        if (found == null) {
            throw new SubjectWasNotCoveredException(other.getId());
        }
    }

    public void assignExam(Exam exam) {
//        Exam found = null;
//        for (Exam ss : this.getExams()) {
//            if (ss.getId() == exam.getId()) {
//                found = ss;
//                this.exams.remove(ss);
//            }
//        }
//
//        if (found == null) {
//            throw new ExamNotFoundException(exam.getId());
//        }
    }

    public void removeExam(Exam exam) {
        Exam found = null;
        for (Exam ss : this.getExams()) {
            if (ss.getId() == exam.getId()) {
                found = ss;
                this.exams.remove(ss);
            }
        }

        if (found == null) {
            throw new ExamNotFoundException(exam.getId());
        }
    }
}
