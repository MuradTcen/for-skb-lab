package skb.lab.registration.entity.email;

import lombok.Data;
import skb.lab.registration.entity.User;
import skb.lab.registration.entity.message.Message;

@Data
public class EmailContent {

    private final static String SUCCESS_REGISTRATION_MESSAGE = "The user is successfully registered";
    private final static String FAIL_REGISTRATION_MESSAGE = "An error occurred during user registration. Try registering again";

    private String header;
    private Message body;

    private EmailContent() {
    }

    public static EmailContent getEmailContentForRegistration(User user, Message message) {
        var emailContent = new EmailContent();

        emailContent.setHeader(user.getName() + ", this is information about registration");
        emailContent.setBody(message);

        return emailContent;
    }
}
