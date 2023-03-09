package com.javadevs.testingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@Entity(name = "Result")
@Table(name = "results")
@EqualsAndHashCode(exclude = "exam")
@ToString(exclude = "exam")
@Builder
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resultsSequenceGenerator")
    @SequenceGenerator(name = "resultsSequenceGenerator", allocationSize = 100, initialValue = 100,
            sequenceName = "results_sequence_generator")
    @Column(name = "result_id")
    private long id;

    @Column(name = "percentage_result")
    private double percentageResult;
    @Column(name = "time_spent")
    private long timeSpent;
    @Column(name = "try_number")
    private long tryNumber;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "exam_id",
            referencedColumnName = "exam_id",
            foreignKey = @ForeignKey(name = "exam_result_fk")
    )
    private Exam exam;
}