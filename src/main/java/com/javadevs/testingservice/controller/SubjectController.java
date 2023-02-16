package com.javadevs.testingservice.controller;

import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateSubjectCommand;
import com.javadevs.testingservice.model.command.edit.EditQuestionCommand;
import com.javadevs.testingservice.model.command.edit.EditSubjectCommand;
import com.javadevs.testingservice.model.dto.QuestionDto;
import com.javadevs.testingservice.model.dto.SubjectDto;
import com.javadevs.testingservice.service.SubjectService;
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
@RequestMapping("api/v1/subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Subject> saveSubject(@RequestBody @Valid CreateSubjectCommand command) {
        log.info("saveSubject{}", command);
        return new ResponseEntity(modelMapper
                .map(subjectService.saveSubject(command), SubjectDto.class), HttpStatus.CREATED);
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity findSubjectById(@PathVariable("subjectId") long subjectId) {
        log.info("findSubjectById({})", subjectId);
        return new ResponseEntity(modelMapper
                .map(subjectService.findSubjectById(subjectId),SubjectDto.class), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity findAllSubjects(@PageableDefault Pageable pageable) {
        log.info("findAllSubjects()");
        return new ResponseEntity(subjectService.findAllSubjects(pageable)
                .map(subject -> modelMapper.map(subject, SubjectDto.class)), HttpStatus.OK);
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity deleteSubjectById(@PathVariable("subjectId") long subjectId) {
        log.info("deleteSubjectById({})", subjectId);
        subjectService.deleteSubject(subjectId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity editSubjectPartially(@PathVariable("subjectId") long subjectId, @RequestBody EditSubjectCommand command) {
        log.info("editSubjectPartially({}, {})", subjectId, command);
        Subject subject = subjectService.editSubjectPartially(subjectId, command);
        return new ResponseEntity(modelMapper.map(subject, SubjectDto.class), HttpStatus.OK);
    }
}
