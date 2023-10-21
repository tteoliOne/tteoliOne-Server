package store.tteolione.tteolione.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.tteolione.tteolione.domain.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
