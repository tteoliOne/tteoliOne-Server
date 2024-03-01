package store.tteolione.tteolione.domain.room.entity;


import jakarta.persistence.*;
import lombok.*;
import store.tteolione.tteolione.domain.product.entity.Product;

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

    public void setExitCreateMember() {
        this.createMember = null;
        this.exitCreateMember = true;
    }

    public void setExitJoinMember() {
        this.joinMember = null;
        this.exitJoinMember = true;
    }
}
