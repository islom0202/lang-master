package org.example.languagemaster.email;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final String from = "turgunpolatovislom5@gmail.com";

    @Async
    public void sendConfirmationCode(String email, String code){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("Confirmation code for LingMaster platform");
        message.setText("Your code: " + code);
        javaMailSender.send(message);
    }
}
