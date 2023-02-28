package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.OptimisticLockException;
import com.javadevs.testingservice.exception.StudentNotFoundException;
import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.StudentSubject;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
import com.javadevs.testingservice.model.command.edit.EditStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.AddSubjectCoveredToStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.DeleteSubjectCoveredFromStudentCommand;
import com.javadevs.testingservice.repository.ExamRepository;
import com.javadevs.testingservice.repository.QuestionRepository;
import com.javadevs.testingservice.repository.StudentRepository;
import com.javadevs.testingservice.repository.StudentSubjectRepository;
import com.javadevs.testingservice.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.JDBCException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;
    private final StudentSubjectRepository ssRepository;
    private final ExamRepository examRepository;

    @Transactional
    public Student saveStudent(CreateStudentCommand command) {
        Student student = modelMapper.map(command, Student.class);
        student.setDummy(0L);
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
        Student student = studentRepository.findOne(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        
        examRepository.deleteByStudentId(id);
        studentRepository.delete(student);
    }

    public Student editStudentPartially(long id, EditStudentCommand command) {
        Student student = studentRepository.findOneWithSubjects(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        ofNullable(command.getName()).ifPresent(student::setName);
        ofNullable(command.getLastname()).ifPresent(student::setLastname);
        ofNullable(command.getEmail()).ifPresent(student::setEmail);
        student.setVersion(command.getVersion());
        studentRepository.save(student);

        return studentRepository.findOneWithSubjects(id).get();
    }

    /*
        problem gdy po usunieciu tematu x, chcemy go znow dodac
     */
    public Student addSubjectCovered(AddSubjectCoveredToStudentCommand cmd) {
        Student student = studentRepository.findOneWithSubjects(cmd.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(cmd.getStudentId()));
        Subject subject = subjectRepository.findOneWithStudents(cmd.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(cmd.getSubjectId()));

        Long oldVersion = student.getVersion();
        student.setVersion(cmd.getVersion());
        student.setDummy(student.getDummy() + 1);

        StudentSubject ss = new StudentSubject(student, subject);
        student.addSubject(ss);
        subject.getStudentSubjects().add(ss);

        ssRepository.save(ss);

        try {
            studentRepository.save(student);
        } catch (ObjectOptimisticLockingFailureException exc) {
            ssRepository.deleteByIds(cmd.getStudentId(), cmd.getSubjectId());
            throw new OptimisticLockException(cmd.getStudentId(), oldVersion, cmd.getVersion());
        }

        //na pewno bedzie objekt, throw polecialby wczesniej
        return studentRepository.findOneWithSubjects(cmd.getStudentId()).get();
    }

    @Transactional
    public void deleteSubjectCovered(DeleteSubjectCoveredFromStudentCommand cmd) {
        Student student = studentRepository.findOneWithSubjects(cmd.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(cmd.getStudentId()));
        Subject subject = subjectRepository.findOneWithStudents(cmd.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(cmd.getSubjectId()));


        //StudentSubject ss = student.deleteSubject(subject);
        ssRepository.deleteByIds(cmd.getStudentId(), cmd.getSubjectId());
    }
}
