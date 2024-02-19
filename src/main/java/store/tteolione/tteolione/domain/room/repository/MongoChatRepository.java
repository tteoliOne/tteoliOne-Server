package store.tteolione.tteolione.domain.room.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import store.tteolione.tteolione.domain.room.entity.mongodb.Chatting;

import java.util.List;

public interface MongoChatRepository extends MongoRepository<Chatting, String> {

    List<Chatting> findByChatRoomNo(Long chatRoomNo);

    Page<Chatting> findByChatRoomNoOrderBySendDateDesc(Long chatRoomNo, Pageable pageable);
}
