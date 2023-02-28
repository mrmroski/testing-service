package com.javadevs.testingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSubjectId implements Serializable {
    @Column(name = "student_student_id")
    private Long studentId;
    @Column(name = "subject_subject_id")
    private Long subjectId;
}
