package com.javadevs.testingservice.service;

import com.javadevs.testingservice.exception.AnswerWasNotAddedException;
import com.javadevs.testingservice.exception.QuestionNotFoundException;
import com.javadevs.testingservice.exception.SubjectNotFoundException;
import com.javadevs.testingservice.exception.WrongQuestionTypeException;
import com.javadevs.testingservice.model.Answer;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.QuestionClosed;
import com.javadevs.testingservice.model.QuestionOpen;
import com.javadevs.testingservice.model.Subject;
import com.javadevs.testingservice.model.command.create.CreateAnswerCommand;
import com.javadevs.testingservice.model.command.create.CreateQuestionClosedCommand;
import com.javadevs.testingservice.model.command.create.CreateQuestionOpenCommand;
import com.javadevs.testingservice.model.command.edit.EditQuestionCommand;
import com.javadevs.testingservice.model.command.questionEdit.AddAnswerCommand;
import com.javadevs.testingservice.model.command.questionEdit.DeleteAnswerCommand;
import com.javadevs.testingservice.repository.AnswerRepository;
import com.javadevs.testingservice.repository.QuestionRepository;
import com.javadevs.testingservice.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-tests")
class QuestionServiceTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionService questionService;

    @BeforeEach
    void clean() {
        subjectRepository.deleteAll();
        questionRepository.deleteAll();
        answerRepository.deleteAll();
    }

    @Test
    void shouldSaveClosedQuestionOnSaveCloseQuestion() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateAnswerCommand answer1 = CreateAnswerCommand.builder()
                .answer("Pampersiki")
                .correct(true).build();

        CreateAnswerCommand answer2 = CreateAnswerCommand.builder()
                .answer("pościel")
                .correct(false).build();

        Set<CreateAnswerCommand> answers = Set.of(answer1, answer2);

        CreateQuestionClosedCommand command = CreateQuestionClosedCommand.builder()
                .question("Jakie najlepsze pieluszki?")
                .subjectId(subject.getId())
                .answers(answers).build();

        QuestionClosed savedQuestion = questionService.saveQuestionClosed(command);

        assertEquals(command.getQuestion(), savedQuestion.getQuestion());
        assertEquals(subject, savedQuestion.getSubject());
        assertEquals(command.getAnswers().size(), savedQuestion.getAnswers().size());
        assertEquals(QuestionClosed.class, savedQuestion.getClass());
    }

    @Test
    void shouldThrowSubjectNotFoundExceptionOnSaveClosedQuestionWhenTryingToSaveClosedQuestionWithNonExistingSubjectId() {
        CreateAnswerCommand answer1 = CreateAnswerCommand.builder()
                .answer("Pampersiki")
                .correct(true).build();

        CreateAnswerCommand answer2 = CreateAnswerCommand.builder()
                .answer("pościel")
                .correct(false).build();

        Set<CreateAnswerCommand> answers = Set.of(answer1, answer2);

        CreateQuestionClosedCommand command = CreateQuestionClosedCommand.builder()
                .question("Jakie najlepsze pieluszki?")
                .subjectId(3421483432L)
                .answers(answers).build();

        assertThrows(SubjectNotFoundException.class, () -> questionService.saveQuestionClosed(command));
    }

    @Test
    void shouldSaveOpenQuestionOnSaveOpenQuestion() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateQuestionOpenCommand command = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(subject.getId())
                .build();

        QuestionOpen savedQuestion = questionService.saveQuestionOpen(command);

        assertEquals(command.getQuestion(), savedQuestion.getQuestion());
        assertEquals(subject, savedQuestion.getSubject());
        assertEquals(command.getAnswer(), savedQuestion.getAnswer());
        assertEquals(QuestionOpen.class, savedQuestion.getClass());
    }

    @Test
    void shouldThrowSubjectNotFoundExceptionOnSaveOpenQuestionWhenTryingToSaveOpenQuestionWithNonExistingSubjectId() {
        CreateQuestionOpenCommand command = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(3824832842L)
                .build();

        assertThrows(SubjectNotFoundException.class, () -> questionService.saveQuestionOpen(command));
    }

    @Test
    void shouldFindQuestionByIdOnFindQuestionById() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateQuestionOpenCommand command = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(subject.getId())
                .build();

        QuestionOpen savedQuestion = questionService.saveQuestionOpen(command);

        QuestionOpen foundQuestion = (QuestionOpen) questionService.findQuestionById(savedQuestion.getId());

        assertEquals(savedQuestion.getQuestion(), foundQuestion.getQuestion());
        assertEquals(savedQuestion.getAnswer(), foundQuestion.getAnswer());
        assertEquals(savedQuestion.getId(), foundQuestion.getId());
        assertEquals(savedQuestion.getSubject(), foundQuestion.getSubject());
        assertEquals(savedQuestion.getVersion(), foundQuestion.getVersion());
    }

    @Test
    void shouldThrowQuestionNotFoundExceptionOnFindQuestionByIdWhenTryingToFindQuestionWithNonExistingId() {
        assertThrows(QuestionNotFoundException.class, () -> questionService.findQuestionById(2342343232432L));
    }

    @Test
    void shouldFindAllQuestionsOnFindAllQuestions() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateQuestionOpenCommand command = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(subject.getId())
                .build();

        CreateQuestionOpenCommand command2 = CreateQuestionOpenCommand.builder()
                .question("Najgorsze pieluszki?")
                .answer("Przeciekające")
                .subjectId(subject.getId())
                .build();

        QuestionOpen savedQuestion = questionService.saveQuestionOpen(command);
        QuestionOpen savedQuestion2 = questionService.saveQuestionOpen(command2);

        List<Question> content = questionService.findAllQuestions(Pageable.unpaged()).getContent();

        assertTrue(content.stream().anyMatch(q -> q.getId() == savedQuestion.getId()));
        assertTrue(content.stream().anyMatch(q -> q.getId() == savedQuestion2.getId()));
    }

    @Test
    void shouldDeleteQuestionOnDeleteQuestion() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateQuestionOpenCommand command = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(subject.getId())
                .build();

        CreateQuestionOpenCommand command2 = CreateQuestionOpenCommand.builder()
                .question("Najgorsze pieluszki?")
                .answer("Przeciekające")
                .subjectId(subject.getId())
                .build();

        QuestionOpen savedQuestion = questionService.saveQuestionOpen(command);
        QuestionOpen savedQuestion2 = questionService.saveQuestionOpen(command2);

        questionService.deleteQuestion(savedQuestion.getId());

        List<Question> questionsAfterDelete = questionService.findAllQuestions(Pageable.unpaged()).getContent();

        assertEquals(1, questionsAfterDelete.size());

        assertTrue(questionsAfterDelete.stream().noneMatch(q -> q.getId() == savedQuestion.getId()));
        assertTrue(questionsAfterDelete.stream().anyMatch(q -> q.getId() == savedQuestion2.getId()));
    }

    @Test
    void shouldThrowQuestionNotFoundExceptionOnDeleteQuestionWhenTryingToDeleteQuestionWithNonExistingId() {
        assertThrows(QuestionNotFoundException.class, () -> questionService.deleteQuestion(21312321L));
    }

    @Test
    void shouldEditQuestionPartiallyOnEditQuestionPartially() {
        Subject subject = new Subject();
        Subject subject2 = new Subject();
        subjectRepository.saveAndFlush(subject);
        subjectRepository.saveAndFlush(subject2);

        CreateQuestionOpenCommand command = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(subject.getId())
                .build();

        QuestionOpen savedQuestion = questionService.saveQuestionOpen(command);

        EditQuestionCommand editQuestionCommand = EditQuestionCommand.builder()
                .question("Najgorsze pieluszki?")
                .version(savedQuestion.getVersion())
                .subjectId(subject2.getId()).build();

        questionService.editQuestionPartially(savedQuestion.getId(), editQuestionCommand);

        QuestionOpen editedQuestion = (QuestionOpen) questionService.findQuestionById(savedQuestion.getId());

        assertEquals(subject2, editedQuestion.getSubject());
        assertEquals("Najgorsze pieluszki?", editedQuestion.getQuestion());
        assertEquals(savedQuestion.getId(), editedQuestion.getId());
        assertEquals(savedQuestion.getAnswer(), editedQuestion.getAnswer());
    }

    @Test
    void shouldThrowQuestionNotFoundExceptionOnEditQuestionPartiallyWhenTryingToEditQuestionWithNonExistingId() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateQuestionOpenCommand command = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(subject.getId())
                .build();

        QuestionOpen savedQuestion = questionService.saveQuestionOpen(command);

        EditQuestionCommand editQuestionCommand = EditQuestionCommand.builder()
                .question("Najgorsze pieluszki?")
                .version(savedQuestion.getVersion()).build();

        assertThrows(QuestionNotFoundException.class,
                () -> questionService.editQuestionPartially(432423432L, editQuestionCommand));
    }

    @Test
    void shouldThrowSubjectNotFoundExceptionOnEditQuestionPartiallyWhenTryingToSetSubjectWithNonExistingId() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateQuestionOpenCommand command = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(subject.getId())
                .build();

        QuestionOpen savedQuestion = questionService.saveQuestionOpen(command);

        EditQuestionCommand editQuestionCommand = EditQuestionCommand.builder()
                .question("Najgorsze pieluszki?")
                .subjectId(2342342343242L)
                .version(savedQuestion.getVersion()).build();

        assertThrows(SubjectNotFoundException.class,
                () -> questionService.editQuestionPartially(savedQuestion.getId(), editQuestionCommand));
    }

    @Test
    void shouldAddAnswerOnAdAnswer() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateAnswerCommand answer1 = CreateAnswerCommand.builder()
                .answer("Pampersiki")
                .correct(true).build();

        CreateAnswerCommand answer2 = CreateAnswerCommand.builder()
                .answer("pościel")
                .correct(false).build();

        Set<CreateAnswerCommand> answers = Set.of(answer1, answer2);

        CreateQuestionClosedCommand command = CreateQuestionClosedCommand.builder()
                .question("Jakie najlepsze pieluszki?")
                .subjectId(subject.getId())
                .answers(answers).build();

        QuestionClosed savedQuestion = questionService.saveQuestionClosed(command);

        AddAnswerCommand addAnswerCommand = AddAnswerCommand.builder()
                .questionId(savedQuestion.getId())
                .answer("Liście")
                .correct(false)
                .build();

        QuestionClosed questionWithAddedAnswer = (QuestionClosed) questionService.addAnswer(addAnswerCommand);

        assertEquals(3, questionWithAddedAnswer.getAnswers().size());
    }

    @Test
    void shouldThrowQuestionNotFoundExceptionOnAddAnswerWhenTryingToAddAnswerToQuestionWithNonExistingQuestionId() {
        AddAnswerCommand addAnswerCommand = AddAnswerCommand.builder()
                .questionId(65465464645L)
                .answer("Liście")
                .correct(false)
                .build();
        assertThrows(QuestionNotFoundException.class, () -> questionService.addAnswer(addAnswerCommand));
    }

    @Test
    void shouldThrowWrongQuestionTypeExceptionOnAddAnswerWhenTryingToAddAnswerToOpenQuestion() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateQuestionOpenCommand command = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(subject.getId())
                .build();

        QuestionOpen savedQuestion = questionService.saveQuestionOpen(command);

        AddAnswerCommand addAnswerCommand = AddAnswerCommand.builder()
                .questionId(savedQuestion.getId())
                .answer("Liście")
                .correct(false)
                .build();

        assertThrows(WrongQuestionTypeException.class, () -> questionService.addAnswer(addAnswerCommand));
    }

    @Test
    void shouldDeleteAnswerOnDeleteAnswer() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateAnswerCommand answer1 = CreateAnswerCommand.builder()
                .answer("Pampersiki")
                .correct(true).build();

        CreateAnswerCommand answer2 = CreateAnswerCommand.builder()
                .answer("pościel")
                .correct(false).build();

        Set<CreateAnswerCommand> answers = Set.of(answer1, answer2);

        CreateQuestionClosedCommand command = CreateQuestionClosedCommand.builder()
                .question("Jakie najlepsze pieluszki?")
                .subjectId(subject.getId())
                .answers(answers).build();

        QuestionClosed savedQuestion = questionService.saveQuestionClosed(command);

        String answerText = "Pampersiki";

        Long answerIdOptional = savedQuestion.getAnswers().stream()
                .filter(a -> a.getAnswer().equals(answerText))
                .map(Answer::getId)
                .findFirst().orElseThrow(NoSuchElementException::new);

        DeleteAnswerCommand deleteAnswerCommand = DeleteAnswerCommand.builder()
                .answerId(answerIdOptional)
                .questionId(savedQuestion.getId()).build();

        questionService.deleteAnswer(deleteAnswerCommand);

        QuestionClosed questionWithDeletedAnswer = (QuestionClosed) questionService.findQuestionById(savedQuestion.getId());

        assertEquals(1, questionWithDeletedAnswer.getAnswers().size());
        assertTrue(questionWithDeletedAnswer.getAnswers().stream().noneMatch(answer -> answer.getAnswer().equals("Pampersiki")));
        assertTrue(questionWithDeletedAnswer.getAnswers().stream().anyMatch(answer -> answer.getAnswer().equals("pościel")));
    }

    @Test
    void shouldThrowQuestionNotFoundExceptionOnDeleteAnswerWhenTryingToDeleteAnswerFromQuestionWhichIdIsNonExisting() {
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateAnswerCommand answer1 = CreateAnswerCommand.builder()
                .answer("Pampersiki")
                .correct(true).build();

        Set<CreateAnswerCommand> answers = Set.of(answer1);

        CreateQuestionClosedCommand command = CreateQuestionClosedCommand.builder()
                .question("Jakie najlepsze pieluszki?")
                .subjectId(subject.getId())
                .answers(answers).build();

        QuestionClosed savedQuestion = questionService.saveQuestionClosed(command);

        String answerText = "Pampersiki";

        Long answerIdOptional = savedQuestion.getAnswers().stream()
                .filter(a -> a.getAnswer().equals(answerText))
                .map(Answer::getId)
                .findFirst().orElseThrow(NoSuchElementException::new);

        DeleteAnswerCommand deleteAnswerCommand = DeleteAnswerCommand.builder()
                .answerId(answerIdOptional)
                .questionId(3243234543534L).build();

        assertThrows(QuestionNotFoundException.class, () -> questionService.deleteAnswer(deleteAnswerCommand));
    }

    @Test
    void shouldThrowAnswerWasNotAddedExceptionWhenTryingToDeleteAnswerWhichWasNotAddedToQuestionWithGivenId(){
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateAnswerCommand answer1 = CreateAnswerCommand.builder()
                .answer("Pampersiki")
                .correct(true).build();

        Set<CreateAnswerCommand> answers = Set.of(answer1);

        CreateQuestionClosedCommand command = CreateQuestionClosedCommand.builder()
                .question("Jakie najlepsze pieluszki?")
                .subjectId(subject.getId())
                .answers(answers).build();

        QuestionClosed savedQuestion = questionService.saveQuestionClosed(command);

        DeleteAnswerCommand deleteAnswerCommand = DeleteAnswerCommand.builder()
                .answerId(543543543543L)
                .questionId(savedQuestion.getId()).build();

        assertThrows(AnswerWasNotAddedException.class, () -> questionService.deleteAnswer(deleteAnswerCommand));
    }

    @Test
    void shouldThrowWrongQuestionTypeExceptionWhenTryingToDeleteAnswerFromOpenQuestion(){
        Subject subject = new Subject();
        subjectRepository.saveAndFlush(subject);

        CreateAnswerCommand answer1 = CreateAnswerCommand.builder()
                .answer("Pampersiki")
                .correct(true).build();

        Set<CreateAnswerCommand> answers = Set.of(answer1);

        CreateQuestionClosedCommand command = CreateQuestionClosedCommand.builder()
                .question("Jakie najlepsze pieluszki?")
                .subjectId(subject.getId())
                .answers(answers).build();

        QuestionClosed savedClsdQuestion = questionService.saveQuestionClosed(command);

        String answerText = "Pampersiki";

        Long answerIdOptional = savedClsdQuestion.getAnswers().stream()
                .filter(a -> a.getAnswer().equals(answerText))
                .map(Answer::getId)
                .findFirst().orElseThrow(NoSuchElementException::new);

        CreateQuestionOpenCommand command1 = CreateQuestionOpenCommand.builder()
                .question("Najlepsze pieluszki?")
                .answer("Nieprzeciekające")
                .subjectId(subject.getId())
                .build();

        QuestionOpen savedQuestion = questionService.saveQuestionOpen(command1);

        DeleteAnswerCommand deleteAnswerCommand = DeleteAnswerCommand.builder()
                .answerId(answerIdOptional)
                .questionId(savedQuestion.getId()).build();

        assertThrows(WrongQuestionTypeException.class, () -> questionService.deleteAnswer(deleteAnswerCommand));
    }
}
