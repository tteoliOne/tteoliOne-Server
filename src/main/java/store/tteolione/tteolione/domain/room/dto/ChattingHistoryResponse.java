package store.tteolione.tteolione.domain.room.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class ChattingHistoryResponse {


    private String loginId; //로그인Id
    private Long productId; //상품Id
    private String title;
    private String productImage; //상품이미지
    private int sharePrice; //공유가격
    private Long opponentId; //상대방 Id(신고를 위해서)
    private String opponentNickname; //상대 닉네임
    private String opponentProfile; //상대 프로필
    private boolean exitOpponent; //상대 채팅방 나감 여부
    private ProductConstants.EProductSoldStatus soldStatus; //상품 상태
    private boolean checkSeller; //판매자인지 판단
    private boolean checkReservation; //로그인유저가 예약중인 상태인지
    private boolean checkReview; //상품이 리뷰가 등록되어있는지
    private List<ChatResponse> chatList; // 채팅 기록들

}
