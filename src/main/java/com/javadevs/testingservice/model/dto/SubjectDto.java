package com.javadevs.testingservice.model.dto;

import com.javadevs.testingservice.controller.SubjectController;
import com.javadevs.testingservice.model.Subject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@EqualsAndHashCode(callSuper = true)
@Data
public class SubjectDto extends RepresentationModel<SubjectDto> {

    private long id;
    private String subject;
    private String description;
    private Long version;

    public SubjectDto(Subject src) {
        this.id = src.getId();
        this.subject = src.getSubject();
        this.description = src.getDescription();
        this.version = src.getVersion();

        this.add(linkTo(methodOn(SubjectController.class).deleteSubjectById(src.getId())).withRel("delete-subject"));
        this.add(linkTo(methodOn(SubjectController.class).editSubjectPartially(src.getId(), null)).withRel("edit-subject"));
    }
}
