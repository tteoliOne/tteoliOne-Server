package store.tteolione.tteolione.domain.room.entity.mongodb;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Document(collection = "chatting")
@Getter
@AllArgsConstructor
@Builder
public class Chatting {

    @Id
    private String id;

    private Long chatRoomNo;
    private Long senderNo;
    private String senderName;
    private String contentType;
    private String content;
    private LocalDateTime sendDate;
    private long readCount;

}
