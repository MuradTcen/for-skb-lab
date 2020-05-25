package skb.lab.registration.service.stub.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import skb.lab.registration.entity.email.EmailAddress;
import skb.lab.registration.entity.email.EmailContent;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class SendMailerStub implements skb.lab.registration.service.stub.SendMailerStub {

    @Override
    public void sendMail(EmailAddress toAddress, EmailContent messageBody) throws TimeoutException {
        if(shouldThrowTimeout()) {
            sleep();

            throw new TimeoutException("Timeout!");
        }

        if(shouldSleep()) {
            sleep();
        }

        // ok.
        log.info("Message sent to {}, body {}.", toAddress, messageBody);
    }

    @SneakyThrows
    public static void sleep() {
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }

    public static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }

    public static boolean shouldThrowTimeout() {
        return new Random().nextInt(10) == 1;
    }
}