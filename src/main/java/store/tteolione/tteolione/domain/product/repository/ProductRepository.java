package store.tteolione.tteolione.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.tteolione.tteolione.domain.category.entity.Category;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;
import store.tteolione.tteolione.domain.product.entity.Product;

import java.util.List;

import static store.tteolione.tteolione.domain.product.constants.ProductConstants.*;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    @Query("select p from Product p join p.category where p.category = :category and p.soldStatus = :eProductSoldStatus order by p.updateAt desc")
    List<Product> findByCategoryAndSoldStatus(@Param("category") Category category, @Param("eProductSoldStatus") EProductSoldStatus eProductSoldStatus);

}
