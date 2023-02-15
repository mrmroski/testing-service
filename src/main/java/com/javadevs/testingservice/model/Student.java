package com.javadevs.testingservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@ToString(exclude = {"subjectsCovered"})
@EqualsAndHashCode(exclude = {"subjectsCovered"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Student")
@Table(name = "students")
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentSequenceGenerator")
    @SequenceGenerator(name = "studentSequenceGenerator", allocationSize = 100, initialValue = 10,
            sequenceName = "student_sequence_generator")
    @Column(name = "student_id")
    private long id;
    private String name;
    private String lastname;
    private String email;
    private LocalDate startedAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "student_subject", joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjectsCovered;
}
