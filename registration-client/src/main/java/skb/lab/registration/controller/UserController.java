package skb.lab.registration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skb.lab.registration.dto.UserDto;
import skb.lab.registration.service.activemq.impl.RegistrationServiceWithActivemqImpl;
import skb.lab.registration.service.stub.impl.RegistrationServiceWithStubsImpl;
import skb.lab.registration.service.simpleRabbitmq.impl.RegistrationServiceWithRabbitmqImpl;

@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final RegistrationServiceWithStubsImpl registrationServiceWithStubs;
    private final RegistrationServiceWithActivemqImpl registrationServiceWithActivemq;
    private final RegistrationServiceWithRabbitmqImpl registrationServiceWithRabbitmq;

    @PostMapping(value = "register-via-stubs", consumes = {"application/json"})
    public void registerViaStub(@Validated @RequestBody UserDto userDto) {
        log.info("Registration for user  " + userDto.getLogin());
        registrationServiceWithStubs.register(userDto);
    }

    @PostMapping(value = "register-via-rabbitmq", consumes = {"application/json"})
    public void registerViaRabbitmq(@Validated @RequestBody UserDto userDto) {
        log.info("Registration for user  " + userDto.getLogin());
        registrationServiceWithRabbitmq.register(userDto);
    }


    @PostMapping(value = "register-via-activemq", consumes = {"application/json"})
    public void registerViaActivemq(@Validated @RequestBody UserDto userDto) {
        log.info("Registration for user  " + userDto.getLogin());
        registrationServiceWithActivemq.register(userDto);
    }
}
