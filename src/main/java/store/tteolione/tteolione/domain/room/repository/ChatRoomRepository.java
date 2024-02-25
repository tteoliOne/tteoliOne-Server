package store.tteolione.tteolione.domain.room.repository;


import org.springframework.data.repository.CrudRepository;
import store.tteolione.tteolione.domain.room.entity.redis.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, String> {
    List<ChatRoom> findByChatRoomNo(Long chatRoomNo);

    Optional<ChatRoom> findByChatRoomNoAndLoginId(Long chatRoomNo, String loginId);

}
