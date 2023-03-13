package com.javadevs.testingservice.model;

import com.javadevs.testingservice.exception.AnswerWasNotAddedException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

class QuestionClosedTest {

    @Test
    public void shouldDeleteAnswerOnDeleteAnswerFromSetOfAnswers() {
        QuestionClosed question = new QuestionClosed();

        Answer answer1 = new Answer();
        answer1.setId(1L);
        answer1.setAnswer("tak");
        answer1.setQuestion(question);

        Answer answer2 = new Answer();
        answer2.setId(2L);
        answer2.setAnswer("nie");
        answer2.setQuestion(question);

        Set<Answer> answers = new HashSet<>();
        answers.add(answer1);
        answers.add(answer2);
        question.setAnswers(answers);

        question.deleteAnswer(answer1.getId());

        System.out.println(question.getAnswers());

        assertThat(question.getAnswers()).containsExactly(answer2);
    }

    @Test
    public void shouldThrowAnswerWasNotAddedExceptionWhenTryingToDeleteAnswerWhichWasNotAddedToQuestion(){
        QuestionClosed question = new QuestionClosed();

        Answer answer1 = new Answer();
        answer1.setId(1L);
        answer1.setAnswer("tak");
        answer1.setQuestion(question);

        Answer answer2 = new Answer();
        answer2.setId(2L);
        answer2.setAnswer("nie");
        answer2.setQuestion(question);

        Set<Answer> answers = new HashSet<>();
        answers.add(answer1);
        question.setAnswers(answers);

        question.deleteAnswer(answer1.getId());

        System.out.println(question.getAnswers());

       assertThrows(AnswerWasNotAddedException.class,()->question.deleteAnswer(answer2.getId()));
    }

}