package store.tteolione.tteolione.domain.product.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Query("select p from Product p join fetch p.user where p.productId = :productId and p.status='A'")
    Optional<Product> findByDetailProduct(@Param("productId") Long productId);

    @Query("select p from Product p join fetch p.user where p.productId = :productId")
    Optional<Product> findByUserAndProductId(@Param("productId") Long productId);

    @Query("select p from Product p join fetch p.user where p.user = :user and p.status='A'")
    List<Product> findByUser(@Param("user") User user);

    @Query("select count(*) from Product p join p.user u where u = :user and p.soldStatus = :soldStatus and p.status='A'")
    int countByUserAndSoldStatus(@Param("user") User user, @Param("soldStatus") ProductConstants.EProductSoldStatus soldStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.productId = :productId")
    Product findByIdWithLock(@Param("productId") Long productId);
}
