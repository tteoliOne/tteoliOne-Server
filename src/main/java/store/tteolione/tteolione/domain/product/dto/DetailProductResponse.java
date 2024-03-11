package store.tteolione.tteolione.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import store.tteolione.tteolione.domain.likes.entity.Likes;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static store.tteolione.tteolione.domain.product.constants.ProductConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailProductResponse {

    private Long productId; //상품id
    private Long categoryId;
    private List<String> images; //상품사진
    private String sellerProfile; //판매자 프로필
    private String receipt;//영수증사진
    private Long sellerId;
    private String sellerNickname; //판매자 닉네임
    private String title; //제목
    private LocalDateTime buyDate;//구매일자
    private int likeCount;//좋아요수
    private int buyCount;//구입수량
    private int buyPrice;//구입가격
    private int shareCount;//공유수량
    private int sharePrice;//공유가격
    private String description;//상세설명
    private double longitude; //경도
    private double latitude; //위도
    private Long likeId; //좋아요id
    private boolean checkLiked; //좋아요 유무
    private boolean checkOwner; //조회한 사람이 판매자인지
    private EProductSoldStatus soldStatus; //판매 상태
    //해시태그

    public static DetailProductResponse toData(Product product, User buyer, List<String> productImages, String receiptImage, Likes likes) {
        return builder()
                .productId(product.getProductId())
                .categoryId(product.getCategory().getCategoryId())
                .images(productImages)
                .sellerProfile(product.getUser().getProfile())
                .receipt(receiptImage)
                .sellerId(product.getUser().getUserId())
                .sellerNickname(product.getUser().getNickname())
                .title(product.getTitle())
                .buyDate(product.getBuyDate())
                .likeCount(product.getLikeCount())
                .buyCount(product.getBuyCount())
                .buyPrice(product.getBuyPrice())
                .shareCount(product.getShareCount())
                .sharePrice(product.getSharePrice())
                .description(product.getDescription())
                .longitude(product.getLongitude())
                .latitude(product.getLatitude())
                .likeId(likes != null ? likes.getLikeId() : null)
                .checkLiked(likes != null)
                .checkOwner(buyer.getUserId().equals(product.getUser().getUserId()))
                .soldStatus(product.getSoldStatus())
                .build();
    }

}
