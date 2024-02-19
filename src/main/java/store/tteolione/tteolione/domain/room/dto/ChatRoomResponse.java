package store.tteolione.tteolione.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
public class ChatRoomResponse {

    private Long chatNo;

    private Long createMember;
    private Long joinMember;

    private Long productNo;
    private String productTitle;
    private long regDate;
    private Participant participant;
    private LatestMessage latestMessage;
    private Long unReadCount;

    public void setUnReadCount(Long unReadCount) {
        this.unReadCount = unReadCount;
    }

    public ChatRoomResponse(Long chatNo, Long createMember, Long joinMember, Long productNo, String productTitle,
                            LocalDateTime regDate, Participant participant) {
        this.chatNo = chatNo;
        this.createMember = createMember;
        this.joinMember = joinMember;
        this.productNo = productNo;
        this.regDate = regDate.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.participant = participant;
        this.productTitle = productTitle;
    }

    public void setLatestMessage(LatestMessage latestMessage) {
        this.latestMessage = latestMessage;
    }

    @Getter
    @ToString
    public static class Participant {
        private String username;
        private String profile;

        public Participant(String username, String profile) {
            this.username = username;
            this.profile = profile;
        }
    }

    @Getter
    @ToString
    public static class LatestMessage {
        private String context;
        private long sendAt;

        @Builder
        public LatestMessage(String context, LocalDateTime sendAt) {
            this.context = context;
            this.sendAt = sendAt.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        }
    }
}
