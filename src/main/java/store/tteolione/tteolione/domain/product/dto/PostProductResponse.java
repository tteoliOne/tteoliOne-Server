package store.tteolione.tteolione.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import store.tteolione.tteolione.domain.product.entity.Product;

@Getter
@Builder
@AllArgsConstructor
public class PostProductResponse {
    private Long productId;
    private Long userId;
    private String result;

    @Builder
    public static PostProductResponse from(Product product){
        return PostProductResponse.builder()
                .productId(product.getProductId())
                .userId(product.getUser().getUserId())
                .result("정상적으로 게시되었습니다.")
                .build();
    }
}
