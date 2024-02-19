package store.tteolione.tteolione.domain.room.dto.chat;


import lombok.*;
import store.tteolione.tteolione.domain.room.entity.mongodb.Chatting;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    private String id;

    @NotNull
    private Long chatRoomNo;

    @NotNull
    private String contentType;

    @NotNull
    private String content;

    private String senderName;

    private Long senderNo;

    @NotNull
    private Long productNo;

    private long sendTime;
    private Integer readCount;
    private String senderLoginId;

    public void setSendTimeAndSender(LocalDateTime sendTime, Long senderNo, String senderName, String senderLoginId, Integer readCount) {
        this.senderName = senderName;
        this.sendTime = sendTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.senderNo = senderNo;
        this.senderLoginId = senderLoginId;
        this.readCount = readCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Chatting toEntity() {
        return Chatting.builder()
                .senderName(senderName)
                .senderNo(senderNo)
                .chatRoomNo(chatRoomNo)
                .contentType(contentType)
                .content(content)
                .sendDate(Instant.ofEpochMilli(sendTime).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                .readCount(readCount)
                .build();
    }
}
