package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.dto.SubjectDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class SubjectToSubjectDtoConverter implements Converter<Subject, SubjectDto> {
    @Override
    public SubjectDto convert(MappingContext<Subject, SubjectDto> mappingContext) {
        Subject subject = mappingContext.getSource();
        return new SubjectDto(subject);
    }
}
