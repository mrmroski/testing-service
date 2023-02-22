package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateSubjectCommand;
import com.javadevs.testingservice.model.command.edit.EditSubjectCommand;
import com.javadevs.testingservice.repository.StudentRepository;
import com.javadevs.testingservice.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

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
    //TODO ida 3 sqle do bazy
    public void deleteSubject(long id) {
        if (subjectRepository.existsById(id)) {
            List<Student> students = studentRepository.findBySubjectId(id);
            students.forEach(x -> x.getSubjectsCovered().removeIf(s -> s.getId() == id));
            subjectRepository.deleteById(id);
        } else {
            throw new SubjectNotFoundException(id);
        }
    }

    public Subject editSubjectPartially(long id, EditSubjectCommand cmd) {

        Subject subject = subjectRepository.findSubjectById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));

        ofNullable(cmd.getSubject()).ifPresent(subject::setSubject);
        ofNullable(cmd.getDescription()).ifPresent(subject::setDescription);
        ofNullable(cmd.getVersion()).ifPresent(subject::setVersion);

        return subjectRepository.saveAndFlush(subject);
    }
}
