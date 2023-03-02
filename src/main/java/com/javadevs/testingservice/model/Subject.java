package com.javadevs.testingservice.model;

import com.javadevs.testingservice.exception.SubjectIsAlreadyCoveredException;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Set;

@ToString(exclude = {"questions", "students"})
@EqualsAndHashCode(exclude = {"questions", "students"})
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


    @ManyToMany(mappedBy = "subjects", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Student> students;

    @OneToMany(mappedBy = "subject", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Question> questions;

    private boolean deleted;

    @Version
    private long version;

//    public void addStudent(Student other) {
//        boolean noneIdMatch = this.students.stream()
//                .noneMatch(curr -> curr.getId() == other.getId());
//
//        if (noneIdMatch) {
//            this.students.add(other);
//        } else {
//            throw new SubjectIsAlreadyCoveredException(other.getId());
//        }
//    }
}
