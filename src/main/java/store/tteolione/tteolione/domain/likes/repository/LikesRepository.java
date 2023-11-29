package store.tteolione.tteolione.domain.likes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.tteolione.tteolione.domain.likes.constants.LikesConstants;
import store.tteolione.tteolione.domain.likes.entity.Likes;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByProductAndUser(Product product, User user);
}
