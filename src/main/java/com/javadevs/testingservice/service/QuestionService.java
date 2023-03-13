package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.AnswerWasNotAddedException;
import com.javadevs.testingservice.exception.QuestionNotFoundException;
import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.exception.WrongQuestionTypeException;
import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.QuestionClosed;
import com.javadevs.testingservice.model.QuestionOpen;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateQuestionClosedCommand;
import com.javadevs.testingservice.model.command.create.CreateQuestionOpenCommand;
import com.javadevs.testingservice.model.command.edit.EditQuestionCommand;
import com.javadevs.testingservice.model.command.questionEdit.AddAnswerCommand;
import com.javadevs.testingservice.model.command.questionEdit.DeleteAnswerCommand;
import com.javadevs.testingservice.repository.AnswerRepository;
import com.javadevs.testingservice.repository.QuestionRepository;
import com.javadevs.testingservice.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public QuestionOpen saveQuestionOpen(CreateQuestionOpenCommand command) {
        Subject subject = subjectRepository.findSubjectById(command.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(command.getSubjectId()));

        QuestionOpen q = new QuestionOpen();
        q.setQuestion(command.getQuestion());
        q.setSubject(subject);
        q.setAnswer(command.getAnswer());

        return questionRepository.save(q);
    }

    @Transactional
    public QuestionClosed saveQuestionClosed(CreateQuestionClosedCommand command) {
        Subject subject = subjectRepository.findSubjectById(command.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(command.getSubjectId()));

        QuestionClosed q = new QuestionClosed();
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

        q.getExams().forEach(e -> e.getQuestions().removeIf(qs -> qs.getId() == id));
        questionRepository.deleteById(id);
    }

    public Question editQuestionPartially(long id, EditQuestionCommand cmd) {
        Question s = questionRepository.findOneWithAnswersSubject(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        Optional.ofNullable(cmd.getSubjectId()).ifPresent(x -> {
            Subject sub = subjectRepository.findSubjectById(x)
                    .orElseThrow(() -> new SubjectNotFoundException(cmd.getSubjectId()));
            s.setSubject(sub);
        });
        Optional.ofNullable(cmd.getQuestion()).ifPresent(s::setQuestion);
        ;
        s.setVersion(cmd.getVersion());


        return questionRepository.save(s);
    }

    @Transactional
    public Question addAnswer(AddAnswerCommand cmd) {
        Question qUnk = questionRepository.findOneWithAnswersSubject(cmd.getQuestionId())
                .orElseThrow(() -> new QuestionNotFoundException(cmd.getQuestionId()));
        if (qUnk instanceof QuestionOpen) {
            throw new WrongQuestionTypeException(qUnk.getClass().toString());
        }
        QuestionClosed q = (QuestionClosed) qUnk;

        Answer ans = new Answer();
        ans.setQuestion(q);
        ans.setAnswer(cmd.getAnswer());
        ans.setCorrect(cmd.getCorrect());

        //nie ma szans ze sie powtorzy id, question zarzadza answer
        q.getAnswers().add(ans);

        return questionRepository.save(q);
    }

    @Transactional

    public void deleteAnswer(DeleteAnswerCommand cmd) {
        Question qUnk = questionRepository.findOneWithAnswersSubject(cmd.getQuestionId())

                .orElseThrow(() -> new QuestionNotFoundException(cmd.getQuestionId()));
        Answer a = answerRepository.findById(cmd.getAnswerId())
                .orElseThrow(() -> new AnswerWasNotAddedException(cmd.getAnswerId()));

        if (qUnk instanceof QuestionOpen) {
            throw new WrongQuestionTypeException(qUnk.getClass().toString());
        }
        QuestionClosed q = (QuestionClosed) qUnk;
        q.deleteAnswer(cmd.getAnswerId());
        //sprawdzone czy ten question mial rzeczywiscie ten answer, oraz zaktualizowany stan
        answerRepository.delete(a);
    }
}
