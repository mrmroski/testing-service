package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.StudentNotFoundException;
import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
import com.javadevs.testingservice.model.command.edit.EditStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.AddSubjectCoveredToStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.DeleteSubjectCoveredFromStudentCommand;
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
    private final ModelMapper modelMapper;

    @Transactional
    public Student saveStudent(CreateStudentCommand command) {
        Student student = modelMapper.map(command, Student.class);
        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public Student findStudentById(long id) {
        return studentRepository.findOneWithSubjects(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<Student> findAllStudents(Pageable pageable) {
        return studentRepository.findAllWithSubjects(pageable);
    }

    @Transactional
    public void deleteStudent(long id) {
        Student student = studentRepository.findOneWithExams(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        studentRepository.delete(student);
    }

    public Student editStudentPartially(long id, EditStudentCommand command) {
        Student student = studentRepository.findOneWithSubjects(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        ofNullable(command.getName()).ifPresent(student::setName);
        ofNullable(command.getLastname()).ifPresent(student::setLastname);
        ofNullable(command.getEmail()).ifPresent(student::setEmail);
        student.setVersion(command.getVersion());

        return studentRepository.saveAndFlush(student);
    }

    @Transactional
    public Student addSubjectCovered(AddSubjectCoveredToStudentCommand cmd) {
        Student student = studentRepository.findOneWithSubjects(cmd.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(cmd.getStudentId()));
        Subject subject = subjectRepository.findById(cmd.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(cmd.getSubjectId()));

        student.addSubject(subject);
        return studentRepository.save(student);
    }

    @Transactional
    public Student deleteSubjectCovered(DeleteSubjectCoveredFromStudentCommand cmd) {
        Student student = studentRepository.findOneWithSubjects(cmd.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(cmd.getStudentId()));
        Subject subject = subjectRepository.findOneWithStudents(cmd.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(cmd.getSubjectId()));

        student.deleteSubject(subject);
        return studentRepository.save(student);
    }
}
