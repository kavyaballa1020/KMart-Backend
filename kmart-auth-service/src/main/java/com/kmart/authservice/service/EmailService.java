package com.kmart.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationEmail(String to, String name) {
        String subject = "Welcome to KMart!";
        String message = "Hi " + name + ",\n\nYou have successfully registered to KMart! 🎉\n\nThank you,\nKMart Team";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom("your_email@gmail.com"); // same as spring.mail.username

        mailSender.send(mailMessage);
        System.out.println("📧 Email sent successfully to " + to);
    }
}
