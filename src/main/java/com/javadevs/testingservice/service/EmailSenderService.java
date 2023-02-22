package com.javadevs.testingservice.service;

import com.javadevs.testingservice.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

    public void sendPreparingMail(String toEmail, Set<Question> questions) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(toEmail);
        mailMessage.setText(questions.toString());
        mailMessage.setSubject(subjectTestReady);

        javaMailSender.send(mailMessage);
    }

    public void sendStartOfTestMail(String toEmail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(toEmail);
        mailMessage.setText("Test2");
        mailMessage.setSubject(subjectTestStart);

        javaMailSender.send(mailMessage);
    }
}
