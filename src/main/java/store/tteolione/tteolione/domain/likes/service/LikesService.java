package store.tteolione.tteolione.domain.likes.service;


import store.tteolione.tteolione.domain.likes.entity.Likes;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.Optional;

public interface LikesService {

    Optional<Likes> findByProductAndUser(Product product, User user);
    void createLikes(Likes likes);
    void deleteLikes(Likes likes);
}
