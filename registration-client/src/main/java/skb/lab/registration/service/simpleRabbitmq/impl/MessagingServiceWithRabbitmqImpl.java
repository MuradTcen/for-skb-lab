package skb.lab.registration.service.simpleRabbitmq.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import skb.lab.registration.entity.message.Message;
import skb.lab.registration.service.simpleRabbitmq.MessagingServiceWithRabbitmq;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessagingServiceWithRabbitmqImpl implements MessagingServiceWithRabbitmq {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange exchange;

    @Override
    public <T> Message<T> sendAndReceive(Message<T> msg) {
        UUID correlationId = UUID.randomUUID();

        var message = rabbitTemplate.convertSendAndReceive(exchange.getName(),
                "rpc", msg, new CorrelationData(correlationId.toString()));
        log.info(message.toString());

        return (Message<T>) message;
    }
}
