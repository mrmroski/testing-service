package com.javadevs.testingservice.exception.handler;

import com.javadevs.testingservice.exception.AnswerIsAlreadyAddedException;
import com.javadevs.testingservice.exception.AnswerWasNotAddedException;
import com.javadevs.testingservice.exception.ExamAlreadyAssignedException;
import com.javadevs.testingservice.exception.ExamExpiredException;
import com.javadevs.testingservice.exception.ExamNotFoundException;
import com.javadevs.testingservice.exception.ExamResultNotFoundException;
import com.javadevs.testingservice.exception.OptimisticLockException;
import com.javadevs.testingservice.exception.QuestionNotFoundException;
import com.javadevs.testingservice.exception.StudentNotFoundException;
import com.javadevs.testingservice.exception.StudentSubjectsNotCoveredException;
import com.javadevs.testingservice.exception.SubjectIsAlreadyCoveredException;
import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.exception.SubjectWasNotCoveredException;
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
        return ResponseEntity.badRequest().body(new ExamNotFoundResponseBody("EXAM_NOT_FOUND", exc.getId()));
    }

    @ExceptionHandler(StudentSubjectsNotCoveredException.class)
    public ResponseEntity<StudentSubjectNotCoveredResponseBody> handleStudentSubjectNotCoveredException(StudentSubjectsNotCoveredException exc) {
        return ResponseEntity.badRequest().body(new StudentSubjectNotCoveredResponseBody("STUDENT_HAS_NOT_COVERED_SUBJECTS"));
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<OptimisticLockBody> handleOptimisticLockException(OptimisticLockException exc) {
        return ResponseEntity.badRequest().body(new OptimisticLockBody("ENTITY_DIFFERENT_VERSION", exc.getId(), exc.getCurrentVersion(), exc.getProvidedVersion()));
    }

    @ExceptionHandler(ExamResultNotFoundException.class)
    public ResponseEntity<ExamResultNotFoundResponseBody> handleExamResultNotFoundException(ExamResultNotFoundException exc) {
        return ResponseEntity.badRequest().body(new ExamResultNotFoundResponseBody("EXAM_RESULT_NOT_FOUND", exc.getId()));
    }

    @ExceptionHandler(ExamExpiredException.class)
    public ResponseEntity<ExamExpiredBody> handleExamExpiredException(ExamExpiredException exc) {
        return ResponseEntity.badRequest().body(new ExamExpiredBody("EXAM_EXPIRED",exc.getId()));
    }

    record QuestionNotFoundResponseBody(String code, Long questionId) {
    }

    record StudentNotFoundResponseBody(String code, Long studentId) {
    }

    record SubjectNotFoundResponseBody(String code, Long subjectId) {
    }

    record SubjectWasNotCoveredResponseBody(String code, Long subjectId) {
    }

    record SubjectIsAlreadyCoveredResponseBody(String code, Long subjectId) {
    }

    record AnswerIsAlreadyAddedResponseBody(String code, Long answerId) {
    }

    record AnswerWasNotAddedResponseBody(String code, Long answerId) {
    }

    record ExamAlreadyAssignedResponseBody(String code, Long examId) {
    }

    record ExamNotFoundResponseBody(String code, Long examId) {
    }

    record StudentSubjectNotCoveredResponseBody(String code) {
    }

    record ExamResultNotFoundResponseBody(String code, Long examResultId) {
    }

    record OptimisticLockBody(String code, Long id, Long currentVersion, Long providedVersion) {
    }

    record ExamExpiredBody(String code, Long id) {
    }

}
