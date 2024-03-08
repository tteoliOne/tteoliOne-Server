package store.tteolione.tteolione.domain.room.repository.mongodb;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import store.tteolione.tteolione.domain.room.entity.mongodb.Chatting;

import java.util.List;

@Repository
public interface MongoChatRepository extends MongoRepository<Chatting, String> {

    List<Chatting> findByChatRoomNo(Long chatRoomNo);

    Page<Chatting> findByChatRoomNoOrderBySendDateDesc(Long chatRoomNo, Pageable pageable);
}
