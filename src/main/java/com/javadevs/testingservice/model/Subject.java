package com.javadevs.testingservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@ToString(exclude = {"questions"})
@EqualsAndHashCode(exclude = {"questions"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Subject")
@Table(name = "subjects")
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subjectSequenceGenerator")
    @SequenceGenerator(name = "subjectSequenceGenerator", allocationSize = 100, initialValue = 10,
            sequenceName = "subject_sequence_generator")
    @Column(name = "subject_id")
    private long id;
    private String subject;
    private String description;

    @OneToMany(mappedBy = "subject", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<Question> questions;
}
