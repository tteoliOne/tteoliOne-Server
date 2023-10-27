package store.tteolione.tteolione.domain.likes.service;

import store.tteolione.tteolione.domain.likes.entity.Likes;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.user.entity.User;

public interface LikesService {
    Likes modifyLike(User user, Product product);

    int getTotalLikes(Product product);
}
