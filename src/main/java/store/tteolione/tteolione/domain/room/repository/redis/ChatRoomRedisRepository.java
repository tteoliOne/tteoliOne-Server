package store.tteolione.tteolione.domain.room.repository.redis;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import store.tteolione.tteolione.domain.room.entity.redis.ChatRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRedisRepository extends CrudRepository<ChatRoom, String> {
    List<ChatRoom> findByChatRoomNo(Long chatRoomNo);

    Optional<ChatRoom> findByChatRoomNoAndLoginId(Long chatRoomNo, String loginId);

}
