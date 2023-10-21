package store.tteolione.tteolione.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.tteolione.tteolione.domain.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
