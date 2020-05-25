package skb.lab.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJpaRepositories
@Configuration
@EnableJms
public class RegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistrationApplication.class, args);
	}

}
