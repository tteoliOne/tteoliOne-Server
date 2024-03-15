package store.tteolione.tteolione.domain.room.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private Long createMember;

    private Long joinMember;

    private Long productNo;

    private LocalDateTime regDate;

    private boolean exitCreateMember;

    private boolean exitJoinMember;

    private LocalDateTime updateDate;

    public void exitCreateMember() {
//        this.createMember = null;
        this.exitCreateMember = true;
    }

    public void exitJoinMember() {
//        this.joinMember = null;
        this.exitJoinMember = true;
    }

    public void newUpdateDate() {
        this.updateDate = LocalDateTime.now();
    }
}
