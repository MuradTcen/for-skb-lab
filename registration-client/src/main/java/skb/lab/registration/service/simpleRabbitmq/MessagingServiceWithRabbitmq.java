package skb.lab.registration.service.simpleRabbitmq;

import skb.lab.registration.entity.message.Message;

public interface MessagingServiceWithRabbitmq {

    <T> Message<T> sendAndReceive(Message<T> msg);
}
