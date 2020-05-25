package skb.lab.registration.service.activemq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.message.Message;
import skb.lab.registration.service.activemq.impl.MessagingServiceWithActivemqImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
class MessagingServiceWithActivemqImplTest {

    @Autowired
    private MessagingServiceWithActivemqImpl messagingServiceWithActivemq;

    public static User getUser() {
        return new User("login", "password", "email", "name");
    }

    @Test
    public void send_whenSendToValidation_thenResponseInExpectedList() throws Exception {
        var msg = new Message<>(getUser());

        String correlationId = messagingServiceWithActivemq.send(msg);
        var actual = messagingServiceWithActivemq.receive(correlationId);
        var expected = List.of(Message.ofValidationFailed(), Message.ofValidationSuccess());

        assertThat(actual).isIn(expected);
    }

}