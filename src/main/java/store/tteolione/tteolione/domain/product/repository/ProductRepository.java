package store.tteolione.tteolione.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.tteolione.tteolione.domain.category.entity.Category;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.List;

import static store.tteolione.tteolione.domain.product.constants.ProductConstants.*;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Query("select p from Product p join fetch p.user where p.productId = :productId and p.status='A'")
    Product findByDetailProduct(@Param("productId") Long productId);

}
