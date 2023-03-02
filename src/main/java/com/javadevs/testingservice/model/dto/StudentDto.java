package com.javadevs.testingservice.model.dto;

import com.javadevs.testingservice.controller.StudentController;
import com.javadevs.testingservice.model.Student;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentDto extends RepresentationModel<StudentDto> {

    private long id;
    private String name;
    private String lastname;
    private String email;
    private LocalDate startedAt;
    private Set<SubjectDto> subjects;
    private Long version;

    public StudentDto(Student src) {
        this.id = src.getId();
        this.name = src.getName();
        this.lastname = src.getLastname();
        this.email = src.getEmail();
        this.startedAt = src.getStartedAt();

        if (src.getSubjects() != null) {
            this.subjects = src.getSubjects().stream().map(SubjectDto::new).collect(Collectors.toSet());
        } else {
            this.subjects = new HashSet<>();
        }

        this.version = src.getVersion();

        this.add(linkTo(methodOn(StudentController.class).addSubjectCovered(src.getId(), null)).withRel("add-subject"));
        this.add(linkTo(methodOn(StudentController.class).deleteSubject(src.getId(), null)).withRel("delete-subject"));
        this.add(linkTo(methodOn(StudentController.class).editStudentPartially(src.getId(), null)).withRel("edit-student"));
        this.add(linkTo(methodOn(StudentController.class).deleteStudentById(src.getId())).withRel("delete-student"));
    }
}
