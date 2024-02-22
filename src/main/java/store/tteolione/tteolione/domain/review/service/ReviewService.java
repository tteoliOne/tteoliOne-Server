package store.tteolione.tteolione.domain.review.service;

import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.review.entity.Review;
import store.tteolione.tteolione.domain.review.repository.ReviewRepository;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.exception.GeneralException;


public interface ReviewService {

    void save(User seller, Review review);

    boolean existsByProduct(Product product);



}
