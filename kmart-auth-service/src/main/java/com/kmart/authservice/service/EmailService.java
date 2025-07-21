package com.kmart.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final String FROM_EMAIL = "your_email@gmail.com"; // should match spring.mail.username

    // ✅ 1. Vendor Registration - Pending Approval
    public void sendRegistrationEmail(String to, String name) {
        String subject = "Welcome to KMart - Registration Received!";
        String message = "Hi " + name + ",\n\n" +
                "Thank you for registering with KMart as a vendor.\n" +
                "Your account is under review by our admin team.\n\n" +
                "You'll be notified once your account is approved.\n\n" +
                "Best regards,\nKMart Team";

        sendSimpleEmail(to, subject, message);
    }

    // ✅ 2. Vendor Approval Notification
    public void sendApprovalEmail(String to, String name) {
        String subject = "KMart Vendor Account Approved ✅";
        String message = "Hi " + name + ",\n\n" +
                "Congratulations! Your vendor account has been approved.\n" +
                "You can now log in and start using your dashboard.\n\n" +
                "Welcome aboard!\nKMart Team";

        sendSimpleEmail(to, subject, message);
    }

    // ✅ 3. Vendor Rejection Notification
    public void sendRejectionEmail(String to, String name) {
        String subject = "KMart Vendor Account Rejected ❌";
        String message = "Hi " + name + ",\n\n" +
                "Unfortunately, your vendor registration was not approved.\n" +
                "For further queries, please contact our support team.\n\n" +
                "Thank you,\nKMart Team";

        sendSimpleEmail(to, subject, message);
    }

    // ✅ 4. User Welcome Email
    public void sendUserWelcomeEmail(String to, String name) {
        String subject = "Welcome to KMart!";
        String message = "Hi " + name + ",\n\n" +
                "Thanks for signing up as a user on KMart.\n" +
                "We're excited to have you onboard. 🚀\n\n" +
                "Explore amazing deals and products anytime!\n\n" +
                "Cheers,\nKMart Team";

        sendSimpleEmail(to, subject, message);
    }

    // ✅ 5. Admin Welcome Email
    public void sendAdminWelcomeEmail(String to, String name) {
        String subject = "KMart Admin Registration Successful";
        String message = "Hi " + name + ",\n\n" +
                "You've successfully registered as an admin on KMart.\n" +
                "Please login to access the admin dashboard and manage operations.\n\n" +
                "Thank you for supporting the KMart platform.\nKMart Team";

        sendSimpleEmail(to, subject, message);
    }

    // ✅ Common Helper Method
    private void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(FROM_EMAIL);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);

        System.out.println("📧 Email sent successfully to " + to);
    }
}
