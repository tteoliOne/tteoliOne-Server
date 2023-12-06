package store.tteolione.tteolione.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import store.tteolione.tteolione.domain.file.entity.File;
import store.tteolione.tteolione.domain.likes.entity.Likes;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;

import java.util.List;
import java.util.stream.Collectors;

import static store.tteolione.tteolione.domain.product.constants.ProductConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSaveProductResponse {

    List<SaveProduct> products;

    public static PostSaveProductResponse toData(List<Likes> likes) {
        List<SaveProduct> saveProducts = likes.stream()
                .map(SaveProduct::toData)
                .collect(Collectors.toList());

        return PostSaveProductResponse.builder()
                .products(saveProducts)
                .build();
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class SaveProduct {
    private Long likeId;
    private Long productId;
    private String productImage;
    private String title;
    private EProductSoldStatus soldStatus;

    public static SaveProduct toData(Likes likes) {
        return SaveProduct.builder()
                .likeId(likes.getLikeId())
                .productId(likes.getProduct().getProductId())
                .productImage(likes.getProduct().productProfile())
                .title(likes.getProduct().getTitle())
                .soldStatus(likes.getProduct().getSoldStatus())
                .build();
    }
}
