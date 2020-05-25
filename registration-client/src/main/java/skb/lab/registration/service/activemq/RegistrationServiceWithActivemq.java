package skb.lab.registration.service.activemq;

import skb.lab.registration.dto.UserDto;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.message.Message;

import javax.jms.JMSException;
import java.util.concurrent.TimeoutException;

public interface RegistrationServiceWithActivemq {
    void register(UserDto userDto);

    String sendMessageAndGetCorrelationId(User user) throws JMSException;

    Message<User> getValidationResponse(String correlationId) throws TimeoutException;

    void sendEmail(User user, Message<User> message);
}
