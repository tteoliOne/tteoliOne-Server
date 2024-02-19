package store.tteolione.tteolione.domain.room.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateChatRoomRequest {

    @NotNull(message = "상품 Id를 적어주세요.")
    private Long productNo;

}
