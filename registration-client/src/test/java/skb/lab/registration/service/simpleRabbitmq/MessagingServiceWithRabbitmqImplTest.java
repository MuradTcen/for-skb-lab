package skb.lab.registration.service.simpleRabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.message.Message;
import skb.lab.registration.service.simpleRabbitmq.impl.MessagingServiceWithRabbitmqImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
class MessagingServiceWithRabbitmqImplTest {

    @Autowired
    private MessagingServiceWithRabbitmqImpl messagingServiceWithRabbitmq;

    @Test
    public void send_whenSendToValidation_thenResponseInExpectedList() throws Exception {
        var user = new User("login", "password", "email", "name");
        var msg = new Message<>(user);

        var actual = messagingServiceWithRabbitmq.sendAndReceive(msg);
        var expected = List.of(Message.ofValidationFailed(), Message.ofValidationSuccess());

        assertThat(actual).isIn(expected);
    }
}