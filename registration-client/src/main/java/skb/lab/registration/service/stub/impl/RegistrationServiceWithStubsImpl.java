package skb.lab.registration.service.stub.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import skb.lab.registration.dto.UserDto;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.email.EmailAddress;
import skb.lab.registration.entity.email.EmailContent;
import skb.lab.registration.entity.message.Message;
import skb.lab.registration.entity.message.MessageId;
import skb.lab.registration.repository.UserRepository;
import skb.lab.registration.service.RegistrationService;
import skb.lab.registration.service.stub.SendMailerStub;

import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceWithStubsImpl implements RegistrationService {
    private static final long WAIT_TIME = 50L;

    private final UserRepository userRepository;
    private final MessagingServiceStubImpl messagingServiceStubImpl;
    private final SendMailerStub sendMailerStub;

    @Override
    public void register(UserDto userDto) {
        // Можно использовать mapstruct вместо статического метода
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

    public Message<User> sendMessageAndGetValidationResponse(User user) {
        var correlationId = messagingServiceStubImpl.send(new Message<User>(user));
        return getValidationResponse(correlationId, user);
    }

    @Override
    public Message<User> getValidationResponse(MessageId correlationId, User user) {
        Message<User> response;

        try {
            response = messagingServiceStubImpl.receive(correlationId, User.class);
        } catch (TimeoutException e) {
            log.error("An error occurred during user {} registration {}", user.getLogin(), e.getMessage());
            response = Message.ofRegistrationFailed();
        }

        return response;
    }

    @Override
    public void sendEmail(User user, Message<User> message) throws TimeoutException {
        sendMailerStub.sendMail(new EmailAddress(user.getEmail()), EmailContent.getEmailContentForRegistration(user, message));
    }

    @SneakyThrows
    private void sendEmailAgain(User user, Message<User> message) {
        try {
            Thread.sleep(WAIT_TIME);

            sendEmail(user, message);
        } catch (TimeoutException e) {
            log.error("An error occurred during user {} registration {}", user.getLogin(), e.getMessage());
        }
    }

}

