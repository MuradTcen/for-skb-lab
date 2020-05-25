package skb.lab.registration.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    @NotNull
    @Min(3)
    private String login;

    @NotNull
    @Min(8)
    private String password;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Min(3)
    private String name;
}
