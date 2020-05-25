package skb.lab.registration.entity.message;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class MessageId {

    private final UUID uuid;
}
