package com.javadevs.testingservice.service;

import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.CreateSubjectCommand;
import com.javadevs.testingservice.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
