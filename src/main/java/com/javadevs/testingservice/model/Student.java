package com.javadevs.testingservice.model;

import com.javadevs.testingservice.exception.ExamAlreadyAssignedException;
import com.javadevs.testingservice.exception.ExamNotFoundException;
import com.javadevs.testingservice.exception.SubjectIsAlreadyCoveredException;
import com.javadevs.testingservice.exception.SubjectWasNotCoveredException;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDate;
import java.util.Set;

@ToString(exclude = {"studentSubjects", "exams"})
@EqualsAndHashCode(exclude = {"studentSubjects", "exams"})
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

    @OneToMany(mappedBy = "student")
    private Set<StudentSubject> studentSubjects;

    @OneToMany(mappedBy = "student", cascade = {CascadeType.PERSIST})
    private Set<Exam> exams;

    private boolean deleted;

    @Version
    private long version;

    private Long dummy;

    public void addSubject(StudentSubject other) {
        boolean noneIdMatch = this.studentSubjects.stream().map(StudentSubject::getSubject)
                .noneMatch(curr -> curr.getId() == other.getSubject().getId());

        if (noneIdMatch) {
            this.studentSubjects.add(other);
        } else {
            throw new SubjectIsAlreadyCoveredException(other.getSubject().getId());
        }
    }

    public StudentSubject deleteSubject(Subject other) {
        StudentSubject found = null;
        for (StudentSubject ss : this.studentSubjects) {
            if (ss.getSubject().getId() == other.getId()) {
                found = ss;
                this.studentSubjects.remove(ss);
            }
        }

        if (found == null) {
            throw new SubjectWasNotCoveredException(other.getId());
        }

        return found;
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
