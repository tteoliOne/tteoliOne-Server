package store.tteolione.tteolione.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.tteolione.tteolione.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
