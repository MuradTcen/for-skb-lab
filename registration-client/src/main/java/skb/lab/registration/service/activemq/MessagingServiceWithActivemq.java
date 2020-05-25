package skb.lab.registration.service.activemq;

import skb.lab.registration.entity.message.Message;

import javax.jms.JMSException;
import java.util.concurrent.TimeoutException;

public interface MessagingServiceWithActivemq {
    <T> String send(Message<T> msg) throws JMSException;

    <T> Message<T> receive(String messageId) throws TimeoutException;
}
