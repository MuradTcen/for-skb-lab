package skb.lab.registration.service;

import skb.lab.registration.dto.UserDto;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.message.Message;
import skb.lab.registration.entity.message.MessageId;

import java.util.concurrent.TimeoutException;

public interface RegistrationService {
    void register(UserDto userDto);

    Message<User> getValidationResponse(MessageId correlationId, User user);

    void sendEmail(User user, Message<User> message) throws TimeoutException;
}
