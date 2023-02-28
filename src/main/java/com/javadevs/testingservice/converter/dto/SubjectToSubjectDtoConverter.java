package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.controller.SubjectController;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.dto.SubjectDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class SubjectToSubjectDtoConverter implements Converter<Subject, SubjectDto> {
    @Override
    public SubjectDto convert(MappingContext<Subject, SubjectDto> mappingContext) {
        Subject subject = mappingContext.getSource();
        SubjectDto dto = SubjectDto.builder()
                .id(subject.getId())
                .description(subject.getDescription())
                .subject(subject.getSubject())
                .version(subject.getVersion())
                .build();

        dto.add(linkTo(methodOn(SubjectController.class).deleteSubjectById(subject.getId())).withRel("delete-subject"));
        dto.add(linkTo(methodOn(SubjectController.class).editSubjectPartially(subject.getId(), null)).withRel("edit-subject"));

        return dto;
    }
}
