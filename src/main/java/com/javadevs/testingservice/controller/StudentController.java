package com.javadevs.testingservice.controller;

import com.javadevs.testingservice.model.Student;
import com.javadevs.testingservice.model.command.edit.EditStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.AddSubjectCoveredToStudentCommand;
import com.javadevs.testingservice.model.command.create.CreateStudentCommand;
import com.javadevs.testingservice.model.command.studentEdit.DeleteSubjectCoveredFromStudentCommand;
import com.javadevs.testingservice.model.dto.StudentDto;
import com.javadevs.testingservice.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<StudentDto> saveStudent(@RequestBody @Valid CreateStudentCommand command) {
        log.info("saveStudent({})", command);

        return new ResponseEntity<>(modelMapper
                .map(studentService.saveStudent(command), StudentDto.class),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDto> findStudentById(@PathVariable("studentId") long studentId) {
        log.info("findStudentById({})", studentId);

        return new ResponseEntity<>(modelMapper
                .map(studentService.findStudentById(studentId),StudentDto.class),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<Page<StudentDto>> findAllStudents(@PageableDefault Pageable pageable) {
        log.info("findAllStudents()");

        return new ResponseEntity<>(studentService.findAllStudents(pageable)
                .map(student -> modelMapper.map(student, StudentDto.class)),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudentById(@PathVariable("studentId") long studentId) {
        log.info("deleteStudentById({})", studentId);

        studentService.deleteStudent(studentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity editStudentPartially(@PathVariable("studentId") long studentId, @RequestBody EditStudentCommand command) {
        log.info("editStudentPartially({}, {})", studentId, command);
        Student student = studentService.editStudentPartially(studentId, command);
        return new ResponseEntity(modelMapper.map(student, StudentDto.class), HttpStatus.OK);
    }

    @PatchMapping("/{studentId}/addSubject")
    public ResponseEntity<?> addSubjectCovered(@PathVariable("studentId") long id,
                                               @RequestBody @Valid AddSubjectCoveredToStudentCommand cmd) {
        log.info("addSubjectCovered({})", id);

        studentService.addSubjectCovered(cmd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{studentId}/deleteSubject")
    public ResponseEntity<?> deleteSubject(@PathVariable("studentId") long id,
                                               @RequestBody @Valid DeleteSubjectCoveredFromStudentCommand cmd) {
        log.info("deleteSubject({})", id);

        studentService.deleteSubjectCovered(cmd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
