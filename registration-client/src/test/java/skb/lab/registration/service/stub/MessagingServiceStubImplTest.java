package skb.lab.registration.service.stub;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import skb.lab.registration.dto.UserDto;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.message.Message;
import skb.lab.registration.entity.message.MessageId;
import skb.lab.registration.repository.UserRepository;
import skb.lab.registration.service.stub.impl.MessagingServiceStubImpl;
import skb.lab.registration.service.stub.impl.RegistrationServiceWithStubsImpl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext
public class MessagingServiceStubImplTest {

    @Autowired
    private MessagingServiceStubImpl messagingServiceStubImpl;

    private static final String USER_EMAIL = "email.gmail.com";
    private static final String USER_LOGIN = "login";
    private static final String USER_NAME = "name";
    private static final String USER_PASSWORD = "password";

    public static UserDto getUserDto() {
        var userDto = new UserDto();
        userDto.setEmail(USER_EMAIL);
        userDto.setLogin(USER_LOGIN);
        userDto.setName(USER_NAME);
        userDto.setPassword(USER_PASSWORD);

        return userDto;
    }

    @Test
    void send_whenSendToValidation_thenResponseInExpectedList() throws TimeoutException {
        var userDto = getUserDto();
        var user = User.getUserOf(userDto);

        messagingServiceStubImpl.send(new Message<User>(user));

        var actual = messagingServiceStubImpl.receive(new MessageId(UUID.randomUUID()), User.class);
        var expected = List.of(Message.ofValidationSuccess(), Message.ofValidationFailed(), Message.ofRegistrationFailed());

        assertThat(actual).isIn(expected);
    }
}