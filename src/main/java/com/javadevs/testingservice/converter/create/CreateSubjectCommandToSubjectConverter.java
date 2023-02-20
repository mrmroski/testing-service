package com.javadevs.testingservice.converter.create;

import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateSubjectCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class CreateSubjectCommandToSubjectConverter implements Converter<CreateSubjectCommand, Subject> {
    @Override
    public Subject convert(MappingContext<CreateSubjectCommand, Subject> mappingContext) {
        CreateSubjectCommand command = mappingContext.getSource();
        return Subject.builder()
                .subject(command.getSubject())
                .description(command.getDescription())
                .build();

    }
}
