package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.QuestionNotFoundException;
import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateQuestionCommand;
import com.javadevs.testingservice.model.command.edit.EditQuestionCommand;
import com.javadevs.testingservice.model.command.questionEdit.AddAnswerCommand;
import com.javadevs.testingservice.model.command.questionEdit.DeleteAnswerCommand;
import com.javadevs.testingservice.repository.AnswerRepository;
import com.javadevs.testingservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SubjectService subjectService;
    private final ModelMapper mapper;

    @Transactional
    public Question saveQuestion(CreateQuestionCommand command) {
        Subject subject = subjectService.findSubjectById(command.getSubjectId());

        Question q = new Question();
        q.setQuestion(command.getQuestion());
        q.setQuestionType(command.getQuestionType());
        q.setSubject(subject);

        Set<Answer> answers = command.getAnswers().stream()
                        .map(cmd -> {
                            Answer ans = new Answer();
                            ans.setAnswer(cmd.getAnswer());
                            ans.setCorrect(cmd.getCorrect());
                            ans.setQuestion(q);
                            return ans;
                        })
                        .collect(Collectors.toSet());
        q.setAnswers(answers);

        return questionRepository.save(q);
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));
    }

    //TODO: in memory problem (widok)
    @Transactional(readOnly = true)
    public Page<Question> findAllQuestions(Pageable pageable) {
        return questionRepository.findAllWithAnswers(pageable);
    }

    @Transactional
    public void deleteQuestion(long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
        } else {
            throw new QuestionNotFoundException(id);
        }
    }

    @Transactional
    public Question editQuestion(long id, EditQuestionCommand cmd) {
        Question s =  questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));
        Subject sub = subjectService.findSubjectById(cmd.getSubjectId());

        s.setQuestion(cmd.getQuestion());
        s.setQuestionType(cmd.getQuestionType());
        s.setSubject(sub);
        return s;
    }

    @Transactional
    public Question editQuestionPartially(long id, EditQuestionCommand cmd) {
        Question s =  questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        Optional.ofNullable(cmd.getSubjectId()).ifPresent(x -> {
            Subject sub = subjectService.findSubjectById(x);
            s.setSubject(sub);
        });
        Optional.ofNullable(cmd.getQuestion()).ifPresent(s::setQuestion);
        Optional.ofNullable(cmd.getQuestionType()).ifPresent(s::setQuestionType);
        return s;
    }

    @Transactional
    public void addAnswer(AddAnswerCommand cmd) {
        Question s =  questionRepository.findById(cmd.getQuestionId())
                .orElseThrow(() -> new QuestionNotFoundException(cmd.getQuestionId()));

        Answer ans = new Answer();
        ans.setQuestion(s);
        ans.setAnswer(cmd.getAnswer());
        ans.setCorrect(cmd.getCorrect());

        s.addAnswer(ans);
    }

    @Transactional
    public void deleteAnswer(DeleteAnswerCommand cmd) {
        Question s =  questionRepository.findById(cmd.getQuestionId())
                .orElseThrow(() -> new QuestionNotFoundException(cmd.getQuestionId()));

        s.deleteAnswer(cmd.getAnswerId());
        answerRepository.deleteById(cmd.getAnswerId());
    }

    @Transactional(readOnly = true)
    public Set<Question> findQuestionsByIds(Set<Long> ids) {
        return questionRepository.findQuestionsByIds(ids);
    }
}
