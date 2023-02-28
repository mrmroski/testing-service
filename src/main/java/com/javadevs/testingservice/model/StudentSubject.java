package com.javadevs.testingservice.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
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

