package com.javadevs.testingservice.converter.dto;

import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.dto.QuestionDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class QuestionToQuestionDtoConverter implements Converter<Question, QuestionDto> {
    @Override
    public QuestionDto convert(MappingContext<Question, QuestionDto> mappingContext) {
        Question question = mappingContext.getSource();

       return new QuestionDto(question);
    }
}
