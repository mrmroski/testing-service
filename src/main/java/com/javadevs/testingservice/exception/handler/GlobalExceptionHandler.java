package com.javadevs.testingservice.exception.handler;

import com.javadevs.testingservice.exception.*;
import lombok.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<QuestionNotFoundResponseBody> handleQuestionNotFoundException(QuestionNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new QuestionNotFoundResponseBody("QUESTION_NOT_FOUND", exc.getId()));
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<StudentNotFoundResponseBody> handleStudentNotFoundException(StudentNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StudentNotFoundResponseBody("STUDENT_NOT_FOUND", exc.getId()));
    }

    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<SubjectNotFoundResponseBody> handleSubjectNotFoundException(SubjectNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SubjectNotFoundResponseBody("SUBJECT_NOT_FOUND", exc.getId()));
    }

    @ExceptionHandler(SubjectWasNotCoveredException.class)
    public ResponseEntity<SubjectWasNotCoveredResponseBody> handleSubjectWasNotCoveredException(SubjectWasNotCoveredException exc) {
        return ResponseEntity.badRequest().body(new SubjectWasNotCoveredResponseBody("SUBJECT_WAS_NOT_COVERED", exc.getId()));
    }

    @ExceptionHandler(SubjectIsAlreadyCoveredException.class)
    public ResponseEntity<SubjectIsAlreadyCoveredResponseBody> handleSubjectIsAlreadyCoveredException(SubjectIsAlreadyCoveredException exc) {
        return ResponseEntity.badRequest().body(new SubjectIsAlreadyCoveredResponseBody("SUBJECT_IS_ALREADY_COVERED", exc.getId()));
    }

    @ExceptionHandler(AnswerIsAlreadyAddedException.class)
    public ResponseEntity<AnswerIsAlreadyAddedResponseBody> handleAnswerIsAlreadyAddedException(AnswerIsAlreadyAddedException exc) {
        return ResponseEntity.badRequest().body(new AnswerIsAlreadyAddedResponseBody("ANSWER_IS_ALREADY_ADDED", exc.getId()));
    }

    @ExceptionHandler(AnswerWasNotAddedException.class)
    public ResponseEntity<AnswerWasNotAddedResponseBody> handleAnswerWasNotAddedException(AnswerWasNotAddedException exc) {
        return ResponseEntity.badRequest().body(new AnswerWasNotAddedResponseBody("ANSWER_WAS_NOT_ADDED", exc.getId()));
    }

    @ExceptionHandler(ExamAlreadyAssignedException.class)
    public ResponseEntity<ExamAlreadyAssignedResponseBody> handleExamAlreadyAssignedException(ExamAlreadyAssignedException exc) {
        return ResponseEntity.badRequest().body(new ExamAlreadyAssignedResponseBody("EXAM_ALREADY_ASSIGNED_TO_STUDENT", exc.getId()));
    }

    @ExceptionHandler(ExamNotFoundException.class)
    public ResponseEntity<ExamNotFoundResponseBody> handleExamNotFoundException(ExamNotFoundException exc) {
        return ResponseEntity.badRequest().body(new ExamNotFoundResponseBody("EXAM_WITH_ID_NOT_FOUND", exc.getId()));
    }

    @ExceptionHandler(StudentSubjectsNotCoveredException.class)
    public ResponseEntity<StudentSubjectNotCoveredResponseBody> handleStudentSubjectNotCoveredException(StudentSubjectsNotCoveredException exc) {
        return ResponseEntity.badRequest().body(new StudentSubjectNotCoveredResponseBody("STUDENT_HAS_NOT_COVERED_SUBJECTS"));
    }



    @Value
    static class QuestionNotFoundResponseBody {
        String code;
        long questionId;
    }

    @Value
    static class StudentNotFoundResponseBody {
        String code;
        long studentId;
    }

    @Value
    static class SubjectNotFoundResponseBody {
        String code;
        long subjectId;
    }

    @Value
    static class SubjectWasNotCoveredResponseBody {
        String code;
        long subjectId;
    }

    @Value
    static class SubjectIsAlreadyCoveredResponseBody {
        String code;
        long subjectId;
    }

    @Value
    static class AnswerIsAlreadyAddedResponseBody {
        String code;
        long answerId;
    }

    @Value
    static class AnswerWasNotAddedResponseBody {
        String code;
        long answerId;
    }

    @Value
    static class ExamAlreadyAssignedResponseBody {
        String code;
        long answerId;
    }

    @Value
    static class ExamNotFoundResponseBody {
        String code;
        long answerId;
    }

    @Value
    static class StudentSubjectNotCoveredResponseBody {
        String code;
    }

}
