package com.javadevs.testingservice.controller;

import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
import com.javadevs.testingservice.model.command.edit.EditStudentCommand;
import com.javadevs.testingservice.model.command.edit.EditSubjectCommand;
import com.javadevs.testingservice.model.dto.StudentDto;
import com.javadevs.testingservice.model.dto.SubjectDto;
import com.javadevs.testingservice.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/students")
public class StudentController {

    private final StudentService studentService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Student> saveStudent(@RequestBody @Valid CreateStudentCommand command) {
        log.info("saveStudent({})", command);
        return new ResponseEntity(modelMapper
                .map(studentService.saveStudent(command), StudentDto.class), HttpStatus.CREATED);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity findStudentById(@PathVariable("studentId") long studentId) {
        log.info("findStudentById({})", studentId);
        return new ResponseEntity(modelMapper
                .map(studentService.findStudentById(studentId),StudentDto.class), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity findAllStudents(@PageableDefault Pageable pageable) {
        log.info("findAllStudents()");
        return new ResponseEntity(studentService.findAllStudents(pageable)
                .map(student -> modelMapper.map(student, StudentDto.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity deleteStudentById(@PathVariable("studentId") long studentId) {
        log.info("deleteStudentById({})", studentId);
        studentService.deleteStudent(studentId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity editStudentPartially(@PathVariable("studentId") long studentId, @RequestBody EditStudentCommand command) {
        log.info("editStudentPartially({}, {})", studentId, command);
        Student student = studentService.editStudentPartially(studentId, command);
        return new ResponseEntity(modelMapper.map(student, StudentDto.class), HttpStatus.OK);
    }
}
