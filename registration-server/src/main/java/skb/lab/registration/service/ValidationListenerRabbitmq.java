package skb.lab.registration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.message.Message;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationListenerRabbitmq {

    @RabbitListener(queues = "tut.rpc.requests")
    public <T> Message<T> receiveMessage(final Message<User> msg) {
        if (new Random().nextBoolean()) {
            log.info("Validation success for user {}", msg.getObject().getLogin());
            return Message.ofValidationSuccess();
        } else {
            log.info("Validation failed for user {}", msg.getObject().getLogin());
            return Message.ofValidationFailed();
        }
    }
}
