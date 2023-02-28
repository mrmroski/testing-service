package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.StudentSubject;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateSubjectCommand;
import com.javadevs.testingservice.model.command.edit.EditSubjectCommand;
import com.javadevs.testingservice.repository.AnswerRepository;
import com.javadevs.testingservice.repository.ExamRepository;
import com.javadevs.testingservice.repository.QuestionExamRepository;
import com.javadevs.testingservice.repository.QuestionRepository;
import com.javadevs.testingservice.repository.StudentRepository;
import com.javadevs.testingservice.repository.StudentSubjectRepository;
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
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final StudentSubjectRepository ssRepository;
    private final QuestionExamRepository questionExamRepository;

    @Transactional
    public Subject saveSubject(CreateSubjectCommand command) {
        Subject subject = modelMapper.map(command, Subject.class);
        return subjectRepository.save(subject);
    }

    @Transactional(readOnly = true)
    public Subject findSubjectById(long id) {
        return subjectRepository.findSubjectById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<Subject> findAllSubjects(Pageable pageable) {
        return subjectRepository.findAll(pageable);
    }

    @Transactional
    public void deleteSubject(long id) {

        Subject subject = subjectRepository.findSubjectById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));

        Set<Question> qs = questionRepository.findQuestionsWithSubjectsAnswersBySubjectId(id);

        answerRepository.softDeleteAllByQuestionIds(qs.stream().map(Question::getId).collect(Collectors.toSet()));
        questionRepository.softDeleteAllById(qs.stream().map(Question::getId).collect(Collectors.toSet()));
        questionExamRepository.softDeleteAllByQuestionIds(qs.stream().map(Question::getId).collect(Collectors.toSet()));
        ssRepository.softDeleteAllBySubjectId(id);

        subjectRepository.delete(subject);
    }

    public Subject editSubjectPartially(long id, EditSubjectCommand cmd) {
        Subject s = subjectRepository.findSubjectById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));

        Optional.ofNullable(cmd.getSubject()).ifPresent(s::setSubject);
        Optional.ofNullable(cmd.getDescription()).ifPresent(s::setDescription);

        s.setVersion(cmd.getVersion());

        return subjectRepository.save(s);
    }
}
