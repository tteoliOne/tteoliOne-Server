package store.tteolione.tteolione.domain.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.room.entity.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    // 내가 만든 채팅방또는 내가 참여중인 채팅방을 전부 찾아주는 메서드
    @Query("select c from Chat c where c.createMember = :memberNo or c.joinMember = :memberNo")
    List<Chat> findChattingRoom(@Param("memberNo") Long memberNo);

    Optional<Chat> findByProductNoAndCreateMember(Long productNo, Long createMember);

    Optional<Chat> findByChatId(Long chatId);

}
