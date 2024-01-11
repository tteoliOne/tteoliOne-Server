package store.tteolione.tteolione.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;
import store.tteolione.tteolione.domain.product.entity.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostProductRequest {

    @NotNull(message = "카테고리 Id를 입력해주세요.")
    private Long categoryId;

    @NotNull(message = "상품 제목을 입력해주세요.")
    private String title;

    @NotNull(message = "구입 가격을 입력해주세요.")
    private int buyPrice;

    @NotNull(message = "구입 수량을 입력해주세요.")
    private int buyCount;

    @NotNull(message = "공유 가격을 입력해주세요.")
    private int sharePrice;

    @NotNull(message = "공유 수량을 입력해주세요.")
    private int shareCount;

    @NotNull(message = "구매 일자를 입력해주세요.")
    private LocalDateTime buyDate;

    @NotNull(message = "상세 설명을 입력해주세요.")
    private String description;

    @NotNull(message = "경도를 입력해주세요.")
    private double longitude;

    @NotNull(message = "위도를 입력해주세요.")
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
