package com.mypanchayat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Temporary storage for OTPs
    private Map<String, String> otpStorage = new HashMap<>();

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(email, otp);
        sendEmail(email, "Your Panchayat OTP Code", "Your verification code is: " + otp);
        return otp;
    }

    public boolean verifyOtp(String email, String enteredOtp) {
        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(enteredOtp)) {
            otpStorage.remove(email);
            return true;
        }
        return false;
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        System.out.println("📧 Email sent to " + to);
    }

    // ✅ ADDED THIS METHOD FOR SARPANCH APPROVAL
    public void sendApprovalEmail(String toEmail, String name, String password) {
        String subject = "Official Account Approved - MyPanchayat";
        String body = "Dear " + name + ",\n\n" +
                "Your official account for MyPanchayat has been verified and approved.\n\n" +
                "Here are your login credentials:\n" +
                "Username: " + toEmail + "\n" +
                "Password: " + password + "\n\n" +
                "Please login and change your password immediately.\n\n" +
                "Best Regards,\nMyPanchayat Admin Team";

        sendEmail(toEmail, subject, body);
    }
}