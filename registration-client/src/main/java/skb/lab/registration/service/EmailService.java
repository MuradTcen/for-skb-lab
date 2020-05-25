package skb.lab.registration.service;

public interface EmailService {
    void sendMessage(String to, String subject, String text);
}