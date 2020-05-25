package skb.lab.registration.service.simpleRabbitmq.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import skb.lab.registration.dto.UserDto;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.message.Message;
import skb.lab.registration.repository.UserRepository;
import skb.lab.registration.service.impl.SendMailerServiceImpl;
import skb.lab.registration.service.simpleRabbitmq.RegistrationServiceWithRabbitmq;

import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceWithRabbitmqImpl implements RegistrationServiceWithRabbitmq {
    private static final long WAIT_TIME = 50L;
    private static final String SUBJECT = "Registration";

    private final UserRepository userRepository;
    private final MessagingServiceWithRabbitmqImpl messagingServiceImpl;
    private final SendMailerServiceImpl sendMailer;

    @Override
    public void register(UserDto userDto) {
        // Можно использовать mapstruct
        var newUser = User.getUserOf(userDto);
        userRepository.save(newUser);
        log.info("User {} saved", newUser.getLogin());

        Message<User> message = sendMessageAndGetValidationResponse(newUser);

        try {
            sendEmail(newUser, message);
        } catch (TimeoutException e) {
            // Не смогли отправить email, попробуем "подождать" и отправить ещё раз
            sendEmailAgain(newUser, message);
        }
    }

    @Override
    public Message<User> sendMessageAndGetValidationResponse(User user) {
        Message<User> message = new Message<>(user);
        return messagingServiceImpl.sendAndReceive(message);
    }

    @Override
    public void sendEmail(User user, Message<User> message) throws TimeoutException {
        sendMailer.sendMessage(user.getEmail(),SUBJECT, message.getMessage());
    }

    @SneakyThrows
    @Override
    public void sendEmailAgain(User user, Message<User> message) {
        try {
            Thread.sleep(WAIT_TIME);

            sendEmail(user, message);
        } catch (TimeoutException e) {
            log.error("A timeout exception during mailing to user {}", user.getLogin());
        }
    }
}
