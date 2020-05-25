package skb.lab.registration.service.activemq.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.message.Message;
import skb.lab.registration.service.activemq.MessagingServiceWithActivemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingServiceWithActivemqImpl implements MessagingServiceWithActivemq {

    private final Destination statusDestination;
    private final JmsTemplate jmsTemplate;

    @Override
    public <T> String send(Message<T> msg) throws JMSException {
        final AtomicReference<javax.jms.Message> message = new AtomicReference<>();

        jmsTemplate.convertAndSend(msg, messagePostProcessor -> {
            message.set(messagePostProcessor);
            return messagePostProcessor;
        });

        String messageId = message.get().getJMSMessageID();
        log.info("sending Message='{}' with MessageId='{}'",
                msg, messageId);

        return messageId;
    }

    @Override
    public <T> Message<T> receive(String messageId) throws TimeoutException {
        Message<T> response = (Message<T>) jmsTemplate.receiveSelectedAndConvert(
                statusDestination,
                "JMSCorrelationID = '" + messageId + "'");

        log.info("receive Message='{}' for CorrelationId='{}'", response, messageId);

        return response;
    }
}
