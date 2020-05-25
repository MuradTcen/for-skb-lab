package skb.lab.registration.service.stub.impl;

import org.springframework.stereotype.Service;
import skb.lab.registration.entity.message.Message;
import skb.lab.registration.entity.message.MessageId;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static skb.lab.registration.service.stub.impl.SendMailerStub.*;

@Service
public class MessagingServiceStubImpl implements skb.lab.registration.service.stub.MessagingServiceStub {

    @Override
    public <T> MessageId send(Message<T> msg) {
        return new MessageId(UUID.randomUUID());
    }

    @Override
    public <T> Message<T> receive(MessageId messageId, Class<T> messageType) throws TimeoutException {
        if (SendMailerStub.shouldThrowTimeout()) {
            sleep();

            throw new TimeoutException("Timeout!");
        }

        if (shouldSleep()) {
            sleep();
        }

        if (new Random().nextBoolean()) {
            return Message.ofValidationSuccess();
        } else {
            return Message.ofValidationFailed();
        }
    }
}
