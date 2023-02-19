package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.StudentNotFoundException;
import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.studentEdit.AddSubjectCoveredToStudentCommand;
import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
import com.javadevs.testingservice.model.command.edit.EditStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.AddSubjectCoveredToStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.DeleteSubjectCoveredFromStudentCommand;
import com.javadevs.testingservice.repository.QuestionRepository;
import com.javadevs.testingservice.repository.StudentRepository;
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
public class StudentService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Student saveStudent(CreateStudentCommand command) {
        Student student = modelMapper.map(command, Student.class);
        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public Student findStudentById(long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
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
            throw new StudentNotFoundException(id);
        }
    }

    public Student editStudentPartially(long id, EditStudentCommand command) {

        Student student = studentRepository.findByIdWithSubjects(id).orElseThrow(()
                -> new RuntimeException(String.format("Student with id %s not found!", id)));

        ofNullable(command.getName()).ifPresent(student::setName);
        ofNullable(command.getLastname()).ifPresent(student::setLastname);
        ofNullable(command.getStartedAt()).ifPresent(student::setStartedAt);
        ofNullable(command.getEmail()).ifPresent(student::setEmail);
        ofNullable(command.getVersion()).ifPresent(student::setVersion);

        return studentRepository.saveAndFlush(student);
    }

    @Transactional
    public void addSubjectCovered(AddSubjectCoveredToStudentCommand cmd) {
        Student student = studentRepository.findById(cmd.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(cmd.getStudentId()));
        Subject subject = subjectRepository.findSubjectById(cmd.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(cmd.getSubjectId()));

        student.addSubject(subject);
    }

    @Transactional
    public void deleteSubjectCovered(DeleteSubjectCoveredFromStudentCommand cmd) {
        Student student = studentRepository.findById(cmd.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(cmd.getStudentId()));
        Subject subject = subjectRepository.findSubjectById(cmd.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(cmd.getSubjectId()));

        student.deleteSubject(subject);
    }
}
