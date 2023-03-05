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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ExamResult")
@Table(name = "exam_results")
@Builder
@SQLDelete(sql = "UPDATE exams SET deleted = true WHERE exam_result_id=?")
@Where(clause = "deleted=false")
@ToString(exclude = "student")
@EqualsAndHashCode(exclude = "student")
public class ExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "examResultsSequenceGenerator")
    @SequenceGenerator(name = "examResultsSequenceGenerator", allocationSize = 100, initialValue = 100,
            sequenceName = "exam_results_sequence_generator")
    @Column(name = "exam_result_id")
    private long id;
    @Column(name = "percentage_result")
    private double percentageResult;
    @Column(name = "time_spent")
    private long timeSpent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "student_id",
            referencedColumnName = "student_id",
            foreignKey = @ForeignKey(name = "student_exam_fk")
    )
    private Student student;

    private boolean deleted;

}
