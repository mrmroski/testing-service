package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.QuestionOpen;
import com.javadevs.testingservice.model.dto.QuestionDto;
import com.javadevs.testingservice.model.dto.QuestionOpenDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class QuestionOpenToQuestionDtoConverter implements Converter<QuestionOpen, QuestionOpenDto> {
    @Override
    public QuestionOpenDto convert(MappingContext<QuestionOpen, QuestionOpenDto> mappingContext) {
        QuestionOpen question = mappingContext.getSource();

       return new QuestionOpenDto(question);
    }
}
