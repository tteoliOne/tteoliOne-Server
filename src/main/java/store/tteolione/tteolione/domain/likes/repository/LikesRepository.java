package store.tteolione.tteolione.domain.likes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.tteolione.tteolione.domain.likes.constants.LikesConstants;
import store.tteolione.tteolione.domain.likes.entity.Likes;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByProductAndUser(Product product, User user);

    @Query("select distinct l from Likes l join fetch l.product p join fetch p.images where l.user = :user and p.status = 'A'")
    List<Likes> savedProducts(@Param("user") User user);

    @Query("select l from Likes l where l.product.productId = :productId AND l.user = :user")
    Optional<Likes> findByProductIdAndUser(@Param("productId") Long productId, @Param("user") User user);

}
