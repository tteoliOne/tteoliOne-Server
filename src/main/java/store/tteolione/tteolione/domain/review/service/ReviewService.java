package store.tteolione.tteolione.domain.review.service;

import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.review.dto.ReviewDto;
import store.tteolione.tteolione.domain.review.entity.Review;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.List;


public interface ReviewService {

    void save(User seller, Review review);

    boolean existsByProduct(Product product);

    List<ReviewDto> listReview(Long userId);
}
