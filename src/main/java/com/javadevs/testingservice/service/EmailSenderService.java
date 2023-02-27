package com.javadevs.testingservice.service;

import com.javadevs.testingservice.model.Question;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Value("${email.subject.test-ready}")
    private String subjectTestReady;

    @Value("${email.subject.test-start}")
    private String subjectTestStart;

    @Value("${spring.mail.username}")
    private String from;

    public void sendPreparingMail(String toEmail, long examId) throws MessagingException {
        VelocityContext context = new VelocityContext();
        context.put("message", "Jak tylko będziesz gotowy przyciśnij przycisk 'WYGENERUJ TEST' by otrzymać kolejnego maila z wygenerowanym testem!");
        context.put("buttonUrl", "http://localhost:8080/api/v1/exams/accept-request/" + examId);

        Template template = new VelocityEngine().getTemplate("src/main/resources/emailTemplate/request.vm");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setFrom(from);
        helper.setSubject(subjectTestReady);
        helper.setText(writer.toString(), true);

        javaMailSender.send(message);
    }

    public void sendStartOfTestMail(String toEmail, Set<Question> questions) throws MessagingException {
        VelocityContext context = new VelocityContext();
        context.put("message", questions);

        Template template = new VelocityEngine().getTemplate("src/main/resources/emailTemplate/success.vm");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setFrom(from);
        helper.setSubject(subjectTestStart);
        String emailContent = writer.toString();
        helper.setText("Twoje pytania to: \n" + emailContent, true);

        javaMailSender.send(message);
    }
}
