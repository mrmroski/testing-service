package com.javadevs.testingservice.model;

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
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Exam")
@Table(name = "exams")
@Builder
@SQLDelete(sql = "UPDATE exams SET deleted = true WHERE exam_id=?")
@Where(clause = "deleted=false")
@ToString(exclude = {"student", "questions"})
@EqualsAndHashCode(exclude = {"student", "questions"})
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "examsSequenceGenerator")
    @SequenceGenerator(name = "examsSequenceGenerator", allocationSize = 100, initialValue = 100,
            sequenceName = "exams_sequence_generator")
    @Column(name = "exam_id")
    private long id;
    private LocalDate createdAt;
    private String description;

    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "student_id",
            referencedColumnName = "student_id",
            foreignKey = @ForeignKey(name = "student_exam_fk")
    )
    private Student student;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "exam_question",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<Question> questions;
}