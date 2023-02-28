package com.javadevs.testingservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "student_subject")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE student_subject SET deleted = true WHERE student_student_id=? AND subject_subject_id=?")
@Where(clause = "deleted=false")
public class StudentSubject {
    @EmbeddedId
    private StudentSubjectId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @JoinColumn(name = "student_student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subjectId")
    @JoinColumn(name = "subject_subject_id")
    private Subject subject;

    private boolean deleted;

    public StudentSubject(Student stu, Subject sub) {
        this.id = new StudentSubjectId(stu.getId(), sub.getId());
        this.student = stu;
        this.subject = sub;
    }
}

