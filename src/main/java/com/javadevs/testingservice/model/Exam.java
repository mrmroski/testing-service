package com.javadevs.testingservice.model;

import com.javadevs.testingservice.exception.AnswerWasNotAddedException;
import com.javadevs.testingservice.exception.QuestionNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
    @SequenceGenerator(name = "examsSequenceGenerator", allocationSize = 100, initialValue = 100,
            sequenceName = "exams_sequence_generator")
    @Column(name = "exam_id")
    private long id;
    private LocalDate createdAt;
    private String description;
    @Version
    private long version;
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", foreignKey = @ForeignKey(name = "student_exam_fk"))
    private Student student;

    @OneToMany(mappedBy = "exam")
    private Set<QuestionExam> questionExams;


}