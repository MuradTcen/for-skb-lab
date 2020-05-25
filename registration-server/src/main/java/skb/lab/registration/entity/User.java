package skb.lab.registration.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import skb.lab.registration.dto.UserDto;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    public User(String login, String password, String email, String name) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    public static User getUserOf(UserDto userDto) {
        var newUser = new User();

        newUser.setEmail(userDto.getEmail());
        newUser.setLogin(userDto.getLogin());
        newUser.setName(userDto.getName());
        newUser.setPassword(userDto.getPassword());

        return newUser;
    }

}
