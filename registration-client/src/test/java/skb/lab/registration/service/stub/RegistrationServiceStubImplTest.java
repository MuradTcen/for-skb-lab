package skb.lab.registration.service.stub;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.email.EmailAddress;
import skb.lab.registration.entity.email.EmailContent;
import skb.lab.registration.repository.UserRepository;
import skb.lab.registration.service.stub.impl.MessagingServiceStubImpl;
import skb.lab.registration.service.stub.impl.RegistrationServiceWithStubsImpl;

import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;
import static skb.lab.registration.service.stub.MessagingServiceStubImplTest.getUserDto;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceStubImplTest {

    @InjectMocks
    private RegistrationServiceWithStubsImpl registrationServiceImpl;

    @Mock
    private UserRepository repository;

    @Mock
    private MessagingServiceStubImpl messagingServiceStub;

    @Mock
    private SendMailerStub sendMailerStub;

    @Test
    void register_whenRegister_thenVerified() throws TimeoutException {
        var userDto = getUserDto();
        var user = User.getUserOf(userDto);
        var emailAddress = new EmailAddress(user.getEmail());
        var emailContent = EmailContent.getEmailContentForRegistration(user, null);

        when(repository.save(user)).thenReturn(user);
        doNothing().when(sendMailerStub).sendMail(emailAddress, emailContent);

        registrationServiceImpl.register(userDto);

        verify(repository).save(user);
        verify(sendMailerStub).sendMail(emailAddress, emailContent);
    }
}