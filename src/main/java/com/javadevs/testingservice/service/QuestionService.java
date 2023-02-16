package com.javadevs.testingservice.service;

import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateQuestionCommand;
import com.javadevs.testingservice.model.command.edit.EditAnswerCommand;
import com.javadevs.testingservice.model.command.edit.EditQuestionCommand;
import com.javadevs.testingservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Optional.ofNullable;

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

    @Transactional
    public Question editQuestionPartially(long id, EditQuestionCommand command) {
        Subject subjectToReplace = null;

        if (command.getSubjectId() != null) {
            subjectToReplace = subjectService.findSubjectById(command.getSubjectId());
        }

        Subject finalSubjectToReplace = subjectToReplace;
        Question question = questionRepository.findById(id)
                .map(questionToEdit -> {
                    ofNullable(command.getCorrectAnswer()).ifPresent(questionToEdit::setCorrectAnswer);
                    ofNullable(command.getQuestionType()).ifPresent(questionToEdit::setQuestionType);
                    ofNullable(command.getQuestion()).ifPresent(questionToEdit::setQuestion);
                    ofNullable(command.getVersion()).ifPresent(questionToEdit::setVersion);
                    ofNullable(finalSubjectToReplace).ifPresent(questionToEdit::setSubject);

                    return questionToEdit;
                }).orElseThrow(()
                        -> new RuntimeException(String.format("Question with id %s not found!", id)));

        return questionRepository.saveAndFlush(question);
    }
}
