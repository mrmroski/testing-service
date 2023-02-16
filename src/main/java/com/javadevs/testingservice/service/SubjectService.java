package com.javadevs.testingservice.service;

import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateSubjectCommand;
import com.javadevs.testingservice.model.command.edit.EditAnswerCommand;
import com.javadevs.testingservice.model.command.edit.EditSubjectCommand;
import com.javadevs.testingservice.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Subject saveSubject(CreateSubjectCommand command) {
        Subject subject = modelMapper.map(command, Subject.class);
        return subjectRepository.save(subject);
    }

    @Transactional(readOnly = true)
    public Subject findSubjectById(long id) {
        return subjectRepository.findSubjectById(id)
                .orElseThrow(() -> new RuntimeException((String.format("Subject with id %s not found!", id))));
    }

    @Transactional(readOnly = true)
    public Page<Subject> findAllSubjects(Pageable pageable) {
        return subjectRepository.findAll(pageable);
    }

    @Transactional
    public void deleteSubject(long id) {
        if (subjectRepository.existsById(id)) {
            subjectRepository.deleteById(id);
        } else {
            throw new RuntimeException(String.format("Subject with id %s not found!", id));
        }
    }

    @Transactional
    public Subject editSubjectPartially(long id, EditSubjectCommand command) {

        Subject subject = subjectRepository.findById(id)
                .map(subjectToEdit -> {
                    ofNullable(command.getSubject()).ifPresent(subjectToEdit::setSubject);
                    ofNullable(command.getDescription()).ifPresent(subjectToEdit::setDescription);
                    ofNullable(command.getVersion()).ifPresent(subjectToEdit::setVersion);

                    return subjectToEdit;
                }).orElseThrow(()
                        -> new RuntimeException(String.format("Subject with id %s not found!", id)));

        return subjectRepository.saveAndFlush(subject);
    }


}
