package com.javadevs.testingservice.service;

import com.javadevs.testingservice.config.MailProperties;
import com.javadevs.testingservice.exception.ExamNotFoundException;
import com.javadevs.testingservice.model.Exam;
import com.javadevs.testingservice.model.Question;
import com.javadevs.testingservice.model.QuestionClosed;
import com.javadevs.testingservice.model.QuestionOpen;
import com.javadevs.testingservice.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final ExamRepository examRepository;
    private final MailProperties mailProperties;

    public void sendPreparingMail(String toEmail, long examId) throws MessagingException {
        VelocityContext context = new VelocityContext();
        context.put("message", "Jak tylko będziesz gotowy przyciśnij przycisk 'WYGENERUJ TEST' by otrzymać kolejnego maila z wygenerowanym testem.");
        context.put("timeMessage", "Od tego czasu zacznie być liczony czas. Powodzenia!");
        context.put("buttonUrl", "http://localhost:8080/api/v1/exams/" + examId + "/generator");

        Template template = new VelocityEngine().getTemplate("src/main/resources/emailTemplate/request.vm");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setFrom(mailProperties.getFrom());
        helper.setSubject(mailProperties.getTitleReady());
        helper.setText(writer.toString(), true);

        CompletableFuture.runAsync(() -> javaMailSender.send(message));
    }

    public void sendStartOfTestMail(String toEmail, Set<Question> questions, long examId) throws MessagingException {
        VelocityContext context = new VelocityContext();

        Set<QuestionClosed> closed = questions.stream()
                .filter(q -> q instanceof QuestionClosed)
                .map(q -> (QuestionClosed) q)
                .collect(Collectors.toSet());
        Set<QuestionOpen> open = questions.stream()
                .filter(q -> q instanceof QuestionOpen)
                .map(q -> (QuestionOpen) q)
                .collect(Collectors.toSet());

        context.put("questions", questions);
        context.put("closed", closed);
        context.put("open", open);
        context.put("examId", examId);

        Template template = new VelocityEngine().getTemplate("src/main/resources/emailTemplate/questions.vm");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setFrom(mailProperties.getFrom());
        helper.setSubject(mailProperties.getTitleStart());
        String emailContent = writer.toString();
        helper.setText("Twoje pytania to: \n" + emailContent, true);

        CompletableFuture.runAsync(() -> javaMailSender.send(message));
    }

    public void sendExamResultToStudent(String email, long examResultId) throws MessagingException {
        double result = getTestResult(examResultId);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getFrom());
        message.setTo(email);
        message.setText(result > 50 ? "Twój wynik to " + result + "%. Test zaliczony :D" : "Twój wynik to " + result + "%. Test niezaliczony! Podejdź niezwłocznie do poprawki.");
        message.setSubject(mailProperties.getTestResult());

        CompletableFuture.runAsync(()->javaMailSender.send(message));

        if (result <= 50) {
            sendPreparingMail(email, examResultId);
        }
    }

    public void sendExamResultToAdmin(String email, long examResultId, long time) {
        double result = getTestResult(examResultId);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getFrom());
        message.setTo(mailProperties.getAdminMail());
        message.setText(result > 50 ? "Wynik " + email + " to " + result + "%, czas " + time + " min. Test zaliczony :D" : "Wynik " + email + " to " + result + "%, czas " + time + " min. Test niezaliczony!");
        message.setSubject(mailProperties.getTestResult());

        CompletableFuture.runAsync(() -> javaMailSender.send(message));
    }

    private double getTestResult(long examResultId) {
        Exam fetchedExamResult = examRepository.findExamById(examResultId).
                orElseThrow(() -> new ExamNotFoundException(examResultId));
        return fetchedExamResult.getResults().stream()
                .filter(r -> r.getTryNumber() == fetchedExamResult.getResults().size())
                .findFirst()
                .get()
                .getPercentageResult();
    }
}
