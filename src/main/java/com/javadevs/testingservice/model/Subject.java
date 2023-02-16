package com.javadevs.testingservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Set;

@ToString(exclude = {"questions"})
@EqualsAndHashCode(exclude = {"questions"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Subject")
@Table(name = "subjects")
@Builder
@SQLDelete(sql = "UPDATE subjects SET deleted = true WHERE subject_id=? AND version=?")
@Where(clause = "deleted=false")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subjectSequenceGenerator")
    @SequenceGenerator(name = "subjectSequenceGenerator", allocationSize = 100, initialValue = 10,
            sequenceName = "subject_sequence_generator")
    @Column(name = "subject_id")
    private long id;
    private String subject;
    private String description;
    private boolean deleted;
    @Version
    private long version;

    @OneToMany(mappedBy = "subject", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Question> questions;
}
