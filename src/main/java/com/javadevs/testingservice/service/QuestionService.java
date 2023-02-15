package com.javadevs.testingservice.service;

import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.CreateQuestionCommand;
import com.javadevs.testingservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SubjectService subjectService;

    @Transactional
    public Question saveQuestion(CreateQuestionCommand command) {
        Subject subject = subjectService.findSubjectById(command.getSubjectId());

        Question question = Question.builder()
                .subject(subject)
                .correctAnswer(command.getCorrectAnswer())
                .question(command.getQuestion())
                .questionType(command.getQuestionType())
                .build();

        return questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException((String.format("Question with id %s not found!", id))));
    }

    @Transactional(readOnly = true)
    public Page<Question> findAllQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @Transactional
    public void deleteQuestion(long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
        } else {
            throw new RuntimeException(String.format("Question with id %s not found!", id));
        }
    }
}
