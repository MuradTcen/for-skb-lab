package skb.lab.registration.service.activemq;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.message.Message;
import skb.lab.registration.repository.UserRepository;
import skb.lab.registration.service.activemq.impl.MessagingServiceWithActivemqImpl;
import skb.lab.registration.service.activemq.impl.RegistrationServiceWithActivemqImpl;
import skb.lab.registration.service.impl.SendMailerServiceImpl;

import javax.jms.JMSException;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;
import static skb.lab.registration.service.stub.MessagingServiceStubImplTest.getUserDto;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceWithActivemqImplTest {

    @InjectMocks
    private RegistrationServiceWithActivemqImpl registrationServiceImpl;

    @Mock
    private UserRepository repository;

    @Mock
    private MessagingServiceWithActivemqImpl messagingServiceWithActivemq;

    @Mock
    private SendMailerServiceImpl sendMailer;

    @Test
    void register_whenRegister_thenVerified() throws TimeoutException, JMSException {
        var userDto = getUserDto();
        var user = User.getUserOf(userDto);
        var correlationId = "correlationId";

        when(repository.save(user)).thenReturn(user);
        doReturn(correlationId).when(messagingServiceWithActivemq).send(any(Message.class));
        doReturn(Message.ofValidationSuccess()).when(messagingServiceWithActivemq).receive(correlationId);
        doNothing().when(sendMailer).sendMessage(any(String.class), any(String.class), any(String.class));

        registrationServiceImpl.register(userDto);

        verify(repository).save(user);
        verify(sendMailer).sendMessage(any(String.class), any(String.class), any(String.class));
    }
}