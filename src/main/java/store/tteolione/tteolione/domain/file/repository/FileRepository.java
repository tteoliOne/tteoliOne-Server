package store.tteolione.tteolione.domain.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.tteolione.tteolione.domain.file.entity.File;
import store.tteolione.tteolione.domain.product.entity.Product;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("select f from File f join f.product where f.product = :product and f.status='A'")
    List<File> findByProductImages(@Param("product") Product product);

}

