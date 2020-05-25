package skb.lab.registration.service.simpleRabbitmq;

import skb.lab.registration.dto.UserDto;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.message.Message;

import java.util.concurrent.TimeoutException;

public interface RegistrationServiceWithRabbitmq {
    void register(UserDto userDto);

    Message<User> sendMessageAndGetValidationResponse(User user);

    void sendEmail(User user, Message<User> message) throws TimeoutException;

    void sendEmailAgain(User user, Message<User> message);
}
