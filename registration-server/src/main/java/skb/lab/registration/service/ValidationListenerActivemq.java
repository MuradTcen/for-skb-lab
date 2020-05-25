package skb.lab.registration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import skb.lab.registration.entity.message.Message;

import javax.jms.Destination;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationListenerActivemq {

    private final Destination statusDestination;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = "${destination.order}")
    public <T> void receive(Message<T> msg, @Header(JmsHeaders.MESSAGE_ID) String messageId) {
        log.info("received message='{}' with MessageId='{}'", msg, messageId);

        log.info("sending response with CorrelationId='{}'", messageId);

        jmsTemplate.send(statusDestination, messageCreator -> {
            var response = new Random().nextBoolean() ?
                    Message.ofValidationSuccess() :
                    Message.ofValidationFailed();

            var message = messageCreator.createObjectMessage(response);
            message.setJMSCorrelationID(messageId);
            return message;
        });
    }
}
