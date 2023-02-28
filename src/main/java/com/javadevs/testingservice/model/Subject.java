package com.javadevs.testingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

import java.util.Set;

@ToString(exclude = {"studentSubjects"})
@EqualsAndHashCode(exclude = {"studentSubjects"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Subject")
@Table(name = "subjects")
@Builder
@SQLDelete(sql = "UPDATE subjects SET deleted = true WHERE subject_id=? and version=?")
@Where(clause = "deleted=false")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subjectSequenceGenerator")
    @SequenceGenerator(name = "subjectSequenceGenerator", allocationSize = 100, initialValue = 100,
            sequenceName = "subject_sequence_generator")
    @Column(name = "subject_id")
    private long id;
    private String subject;
    private String description;


    @OneToMany(mappedBy = "subject")
    private Set<StudentSubject> studentSubjects;

    private boolean deleted;

    @Version
    private long version;
}
