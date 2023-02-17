package com.javadevs.testingservice.service;

import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.studentEdit.AddSubjectCoveredToStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.AssignQuestionToStudentCommand;
import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.DeleteSubjectCoveredFromStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.UnassignQuestionFromStudentCommand;
import com.javadevs.testingservice.repository.QuestionRepository;
import com.javadevs.testingservice.repository.StudentRepository;
import com.javadevs.testingservice.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void addSubjectCovered(AddSubjectCoveredToStudentCommand cmd) {
        Student student = studentRepository.findById(cmd.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student with id " + cmd.getStudentId() + " not found!"));
        Subject subject = subjectRepository.findSubjectById(cmd.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject with id " + cmd.getSubjectId() + " not found!"));

        student.addSubject(subject);
    }

//    @Transactional
//    public void assignQuestion(AssignQuestionToStudentCommand cmd) {
//        Student student = studentRepository.findById(cmd.getStudentId())
//                .orElseThrow(() -> new RuntimeException("Student with id " + cmd.getStudentId() + " not found!"));
//        Question question = questionRepository.findById(cmd.getQuestionId())
//                .orElseThrow(() -> new RuntimeException("Question with id " + cmd.getQuestionId() + " not found!"));
//
//        student.assignQuestion(question);
//    }

    @Transactional
    public void deleteSubjectCovered(DeleteSubjectCoveredFromStudentCommand cmd) {
        Student student = studentRepository.findById(cmd.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student with id " + cmd.getStudentId() + " not found!"));
        Subject subject = subjectRepository.findSubjectById(cmd.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject with id " + cmd.getSubjectId() + " not found!"));

        student.deleteSubject(subject);
    }

//    @Transactional
//    public void unassignQuestion(UnassignQuestionFromStudentCommand cmd) {
//        Student student = studentRepository.findById(cmd.getStudentId())
//                .orElseThrow(() -> new RuntimeException("Student with id " + cmd.getStudentId() + " not found!"));
//        Question question = questionRepository.findById(cmd.getQuestionId())
//                .orElseThrow(() -> new RuntimeException("Question with id " + cmd.getQuestionId() + " not found!"));
//
//        student.unassignQuestion(question);
//    }
}
