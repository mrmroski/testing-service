package com.javadevs.testingservice.converter.create;

import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.command.create.CreateAnswerCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class CreateAnswerCommandToAnswerConverter implements Converter<CreateAnswerCommand, Answer> {
    @Override
    public Answer convert(MappingContext<CreateAnswerCommand, Answer> mappingContext) {
        CreateAnswerCommand cmd = mappingContext.getSource();
        return Answer.builder()
                .answer(cmd.getAnswer())
                .correct(cmd.getCorrect())
                .build();
    }
}
