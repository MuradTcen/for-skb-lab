package skb.lab.registration.service.activemq.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import skb.lab.registration.dto.UserDto;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.message.Message;
import skb.lab.registration.repository.UserRepository;
import skb.lab.registration.service.activemq.RegistrationServiceWithActivemq;
import skb.lab.registration.service.impl.SendMailerServiceImpl;

import javax.jms.JMSException;
import java.util.concurrent.TimeoutException;


@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationServiceWithActivemqImpl implements RegistrationServiceWithActivemq {

    private static final String SUBJECT = "Registration";

    private final UserRepository userRepository;
    private final MessagingServiceWithActivemqImpl messagingServiceWithActivemq;
    private final SendMailerServiceImpl sendMailer;

    @Override
    public void register(UserDto userDto) {
        // Можно использовать mapstruct вместо статического метода
        var newUser = User.getUserOf(userDto);
        userRepository.save(newUser);
        log.info("User {} saved", newUser.getLogin());

        try {
            String messageId = sendMessageAndGetCorrelationId(newUser);
            var message = getValidationResponse(messageId);
            sendEmail(newUser, message);
        } catch (JMSException e) {
            log.error("A timeout exception during sending to validation, user {}", newUser.getLogin());
        } catch (TimeoutException e) {
            log.error("A timeout exception during validation, user {}", newUser.getLogin());
        }
    }

    @Override
    public String sendMessageAndGetCorrelationId(User user) throws JMSException {
        Message<User> message = new Message<>(user);
        return messagingServiceWithActivemq.send(message);
    }

    @Override
    public Message<User> getValidationResponse(String correlationId) throws TimeoutException {
        return messagingServiceWithActivemq.receive(correlationId);
    }

    @Override
    public void sendEmail(User user, Message<User> message) {
        sendMailer.sendMessage(user.getEmail(), SUBJECT, message.getMessage());
    }
}
