package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.QuestionNotFoundException;
import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateQuestionCommand;
import com.javadevs.testingservice.model.command.edit.EditQuestionCommand;
import com.javadevs.testingservice.model.command.questionEdit.AddAnswerCommand;
import com.javadevs.testingservice.model.command.questionEdit.DeleteAnswerCommand;
import com.javadevs.testingservice.repository.AnswerRepository;
import com.javadevs.testingservice.repository.QuestionExamRepository;
import com.javadevs.testingservice.repository.QuestionRepository;
import com.javadevs.testingservice.repository.SubjectRepository;
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
    private final SubjectRepository subjectRepository;
    private final QuestionExamRepository questionExamRepository;

    @Transactional
    public Question saveQuestion(CreateQuestionCommand command) {
        Subject subject = subjectRepository.findSubjectById(command.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(command.getSubjectId()));

        Question q = new Question();
        q.setQuestion(command.getQuestion());
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
        return questionRepository.findOneWithAnswersSubject(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<Question> findAllQuestions(Pageable pageable) {
        return questionRepository.findAllWithAnswersSubject(pageable);
    }

    @Transactional
    public void deleteQuestion(long id) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        questionExamRepository.softDeleteAllByQuestionId(id);
        questionRepository.deleteById(id);
    }

    public Question editQuestionPartially(long id, EditQuestionCommand cmd) {
        Question s = questionRepository.findByIdWithAnswers(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        Optional.ofNullable(cmd.getSubjectId()).ifPresent(x -> {
            Subject sub = subjectRepository.findOneWithStudents(x)
                    .orElseThrow(() -> new SubjectNotFoundException(cmd.getSubjectId()));
            s.setSubject(sub);
        });
        Optional.ofNullable(cmd.getQuestion()).ifPresent(s::setQuestion);;
        s.setVersion(cmd.getVersion());

        questionRepository.saveAndFlush(s);
        return questionRepository.findByIdWithAnswers(id).get();
    }

    public Question addAnswer(AddAnswerCommand cmd) {
        Question q = questionRepository.findOneWithAnswersSubject(cmd.getQuestionId())
                .orElseThrow(() -> new QuestionNotFoundException(cmd.getQuestionId()));

        Answer ans = new Answer();
        ans.setQuestion(q);
        ans.setAnswer(cmd.getAnswer());
        ans.setCorrect(cmd.getCorrect());

        q.addAnswer(ans);
        q.setVersion(cmd.getVersion());
        q.setDummy(q.getDummy() + 1);

        return questionRepository.save(q);
    }

    @Transactional
    public void deleteAnswer(DeleteAnswerCommand cmd) {
        Question s = questionRepository.findByIdWithAnswers(cmd.getQuestionId())
                .orElseThrow(() -> new QuestionNotFoundException(cmd.getQuestionId()));

        s.deleteAnswer(cmd.getAnswerId());
        answerRepository.deleteById(cmd.getAnswerId());
    }

    @Transactional(readOnly = true)
    public Set<Question> findQuestionsByIds(Set<Long> ids) {
        return questionRepository.findQuestionsByIds(ids);
    }
}
