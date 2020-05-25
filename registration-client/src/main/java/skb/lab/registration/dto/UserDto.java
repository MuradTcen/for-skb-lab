package skb.lab.registration.dto;

import lombok.Data;

@Data
public class UserDto {

    private String login;
    private String password;
    private String email;
    private String name;

}
