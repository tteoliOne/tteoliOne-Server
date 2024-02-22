package store.tteolione.tteolione.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.product.entity.ProductTrade;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.Optional;

public interface ProductTradeRepository extends JpaRepository<ProductTrade, Long> {

    Optional<ProductTrade> findByProductAndBuyer(Product product, User buyer);

    boolean existsByProductAndBuyer(Product product, User buyer);

}
