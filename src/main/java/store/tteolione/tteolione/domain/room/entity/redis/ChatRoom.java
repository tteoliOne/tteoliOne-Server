package store.tteolione.tteolione.domain.room.entity.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash(value = "chatRoom")
public class ChatRoom {

    @Id
    private String id;

    @Indexed
    private Long chatRoomNo;

    @Indexed
    private String loginId;

}
