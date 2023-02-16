package com.javadevs.testingservice.service;

import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
import com.javadevs.testingservice.model.command.edit.EditStudentCommand;
import com.javadevs.testingservice.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Student saveStudent(CreateStudentCommand command) {
        Student student = modelMapper.map(command, Student.class);
        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public Student findStudentById(long id) {
        return studentRepository.findByIdWithSubjects(id)
                .orElseThrow(() -> new RuntimeException((String.format("Student with id %s not found!", id))));
    }

    @Transactional(readOnly = true)
    public Page<Student> findAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Transactional
    public void deleteStudent(long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else {
            throw new RuntimeException(String.format("Student with id %s not found!", id));
        }
    }

    @Transactional
    public Student editStudentPartially(long id, EditStudentCommand command) {

        Student student = studentRepository.findByIdWithSubjects(id)
                .map(studentToEdit -> {
                    ofNullable(command.getName()).ifPresent(studentToEdit::setName);
                    ofNullable(command.getLastname()).ifPresent(studentToEdit::setLastname);
                    ofNullable(command.getStartedAt()).ifPresent(studentToEdit::setStartedAt);
                    ofNullable(command.getEmail()).ifPresent(studentToEdit::setEmail);
                    ofNullable(command.getVersion()).ifPresent(studentToEdit::setVersion);

                    return studentToEdit;
                }).orElseThrow(()
                        -> new RuntimeException(String.format("Student with id %s not found!", id)));

        System.out.println(student);

        return studentRepository.saveAndFlush(student);
    }
}
