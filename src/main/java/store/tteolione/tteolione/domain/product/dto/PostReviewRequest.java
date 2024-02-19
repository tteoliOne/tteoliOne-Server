package store.tteolione.tteolione.domain.product.dto;

import lombok.Data;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.review.entity.Review;
import store.tteolione.tteolione.domain.user.entity.User;

import javax.validation.constraints.NotNull;

@Data
public class PostReviewRequest {

    @NotNull
    private String content;

    @NotNull
    private int ddabong;

    public Review toEntity(User user, Product product) {
        return Review.builder()
                .product(product)
                .ddabong(ddabong)
                .content(content)
                .user(user)
                .build();
    }
}
