package store.tteolione.tteolione.domain.product.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.review.entity.Review;
import store.tteolione.tteolione.domain.user.entity.User;

import jakarta.validation.constraints.NotNull;

@Data
public class PostReviewRequest {

    @NotNull(message = "내용을 입력해주세요.")
    private String content;

    @Range(min = 1, max = 5, message = "1이상 5이하로 입력해주세요.")
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
