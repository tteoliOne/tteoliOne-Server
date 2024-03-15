package store.tteolione.tteolione.domain.product.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.tteolione.tteolione.domain.category.entity.Category;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;
import store.tteolione.tteolione.domain.product.dto.ProductDto;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Query("SELECT new store.tteolione.tteolione.domain.product.dto.ProductDto(" +
            "p.productId, " +
            "(SELECT f.fileUrl FROM File f WHERE f.fileId = (SELECT MIN(f2.fileId) FROM File f2 WHERE f2.product = p ORDER BY f2.updateAt ASC)), " +
            "p.title, " +
            "p.sharePrice / p.shareCount, " +
            "p.likeCount, " +
            "p.soldStatus, " +
            "p.longitude, " +
            "p.latitude) " +
            "FROM Product p " +
            "WHERE p.status = 'A' " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND p.soldStatus = 'eNew' " +
            "AND p.createAt BETWEEN :searchStartDate AND :searchEndDate")
    Slice<ProductDto> findByProductDto(
            @Param("category") Category category,
            @Param("searchStartDate") LocalDateTime searchStartDate,
            @Param("searchEndDate") LocalDateTime searchEndDate,
            Pageable pageable);
}
