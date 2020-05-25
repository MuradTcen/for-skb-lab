package skb.lab.registration.service.simpleRabbitmq;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.email.EmailAddress;
import skb.lab.registration.entity.email.EmailContent;
import skb.lab.registration.entity.message.Message;
import skb.lab.registration.repository.UserRepository;
import skb.lab.registration.service.impl.SendMailerServiceImpl;
import skb.lab.registration.service.simpleRabbitmq.impl.MessagingServiceWithRabbitmqImpl;
import skb.lab.registration.service.simpleRabbitmq.impl.RegistrationServiceWithRabbitmqImpl;

import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;
import static skb.lab.registration.service.stub.MessagingServiceStubImplTest.getUserDto;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceWithRabbitmqImplTest {

    @InjectMocks
    private RegistrationServiceWithRabbitmqImpl registrationServiceImpl;

    @Mock
    private UserRepository repository;

    @Mock
    private MessagingServiceWithRabbitmqImpl messagingServiceWithRabbitmq;

    @Mock
    private SendMailerServiceImpl sendMailer;

    @Test
    void register_whenRegister_thenVerified() throws TimeoutException {
        var userDto = getUserDto();
        var user = User.getUserOf(userDto);

        when(repository.save(user)).thenReturn(user);
        when(messagingServiceWithRabbitmq.sendAndReceive(any(Message.class))).thenReturn(Message.ofValidationSuccess());
        doNothing().when(sendMailer).sendMessage(any(String.class), any(String.class), any(String.class));

        registrationServiceImpl.register(userDto);

        verify(repository).save(user);
        verify(sendMailer).sendMessage(any(String.class), any(String.class), any(String.class));
    }
}