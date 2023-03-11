package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.QuestionClosed;
import com.javadevs.testingservice.model.dto.QuestionClosedDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class QuestionClosedToQuestionDtoConverter implements Converter<QuestionClosed, QuestionClosedDto> {
    @Override
    public QuestionClosedDto convert(MappingContext<QuestionClosed, QuestionClosedDto> mappingContext) {
        QuestionClosed question = mappingContext.getSource();
        return new QuestionClosedDto(question);
    }
}
