package store.tteolione.tteolione.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;
import store.tteolione.tteolione.domain.product.entity.Product;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostProductRequest {

    @NotBlank(message = "카테고리 Id를 입력해주세요.")
    private Long categoryId;

    @NotBlank(message = "상품 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "구입 가격을 입력해주세요.")
    private int buyPrice;

    @NotBlank(message = "구입 수량을 입력해주세요.")
    private int buyCount;

    @NotBlank(message = "공유 가격을 입력해주세요.")
    private int sharePrice;

    @NotBlank(message = "공유 수량을 입력해주세요.")
    private int shareCount;

    @NotBlank(message = "구매 일자를 입력해주세요.")
    private LocalDateTime buyDate;

    @NotBlank(message = "상세 설명을 입력해주세요.")
    private String description;

    @NotBlank(message = "경도를 입력해주세요.")
    private double longitude;

    @NotBlank(message = "위도를 입력해주세요.")
    private double latitude;


    public Product toEntity() {
        return Product.builder()
                .title(title)
                .buyPrice(buyPrice)
                .buyCount(buyCount)
                .sharePrice(sharePrice)
                .shareCount(shareCount)
                .buyDate(buyDate)
                .description(description)
                .longitude(longitude)
                .latitude(latitude)
                .likeCount(0)
                .soldStatus(ProductConstants.EProductSoldStatus.eNew)
                .build();
    }
}
