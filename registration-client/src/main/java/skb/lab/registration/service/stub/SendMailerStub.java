package skb.lab.registration.service.stub;

import skb.lab.registration.entity.email.EmailAddress;
import skb.lab.registration.entity.email.EmailContent;

import java.util.concurrent.TimeoutException;

/**
 * Ориентировочный интерфейс мейлера.
 */
public interface SendMailerStub {
    void sendMail (EmailAddress toAddress, EmailContent messageBody) throws TimeoutException;
}