package store.tteolione.tteolione.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.review.entity.Review;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByProduct(Product product);

    @Query("SELECT r FROM Review r JOIN FETCH r.product p JOIN FETCH p.user u WHERE u = :seller")
    List<Review> findBySeller(@Param("seller") User seller);

    @Query("SELECT count(*) FROM Review r JOIN r.product p JOIN p.user u WHERE u = :seller")
    int countBySeller(@Param("seller") User seller);

}
