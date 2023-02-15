package com.javadevs.testingservice.service;

import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.command.CreateAnswerCommand;
import com.javadevs.testingservice.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final ModelMapper modelMapper;
    private final AnswerRepository answerRepository;
    private final QuestionService questionService;

    @Transactional
    public Answer saveAnswer(CreateAnswerCommand command) {
        Question question = questionService.findQuestionById(command.getQuestionId());

        Answer answer = Answer.builder()
                .answer(command.getAnswer())
                .question(question)
                .build();

        return answerRepository.save(answer);
    }

    @Transactional(readOnly = true)
    public Answer findAnswerById(long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException((String.format("Answer with id %s not found!", id))));
    }

    @Transactional(readOnly = true)
    public Page<Answer> findAllAnswers(Pageable pageable) {
        return answerRepository.findAll(pageable);
    }

    @Transactional
    public void deleteAnswer(long id) {
        if (answerRepository.existsById(id)) {
            answerRepository.deleteById(id);
        } else {
            throw new RuntimeException(String.format("Answer with id %s not found!", id));
        }
    }
}
