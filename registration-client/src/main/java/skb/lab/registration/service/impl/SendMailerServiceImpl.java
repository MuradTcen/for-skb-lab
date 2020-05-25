package skb.lab.registration.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import skb.lab.registration.service.EmailService;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMailerServiceImpl implements EmailService {

    public final JavaMailSender emailSender;

    @Override
    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        log.info("Sending email..");
        try {
            emailSender.send(message);
        } catch (MailException e) {
            log.error("A mail exception {}", e.getMessage());
        }
    }
}
