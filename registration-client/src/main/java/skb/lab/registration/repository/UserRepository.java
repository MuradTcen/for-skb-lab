package skb.lab.registration.repository;

import org.springframework.data.repository.CrudRepository;
import skb.lab.registration.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByLogin(String name);
}
