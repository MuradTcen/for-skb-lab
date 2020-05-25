package skb.lab.registration.entity.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message<T> implements Serializable {
    private String message;
    private T object;
    private static String REGISTRATION_TIMEOUT_FAIL = "An error occurred during registration, please try again";
    private static String VALIDATION_FAIL = "A validation failed";
    private static String VALIDATION_SUCCESS = "A validation success";

    public Message(T object) {
        this.object = object;
    }

    public Message() {
    }

    public static <T> Message<T> ofRegistrationFailed() {
        var message = new Message<T>();
        message.setMessage(REGISTRATION_TIMEOUT_FAIL);
        return message;
    }

    public static <T> Message<T> ofValidationFailed() {
        var message = new Message<T>();
        message.setMessage(VALIDATION_FAIL);
        return message;
    }

    public static <T> Message<T> ofValidationSuccess() {
        var message = new Message<T>();
        message.setMessage(VALIDATION_SUCCESS);
        return message;
    }
}
