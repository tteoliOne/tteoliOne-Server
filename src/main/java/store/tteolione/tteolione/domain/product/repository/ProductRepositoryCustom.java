package store.tteolione.tteolione.domain.product.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import store.tteolione.tteolione.domain.category.entity.Category;
import store.tteolione.tteolione.domain.product.dto.ProductDto;
import store.tteolione.tteolione.domain.search.dto.SearchProductResponse;
import store.tteolione.tteolione.domain.user.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepositoryCustom {

    List<ProductDto> findSimpleDtoByProductsUserId(User userId, Category category, double longitude, double latitude);
    Slice<ProductDto> findListProductDtoByProducts(Category category, User user, double longitude, double latitude, LocalDate searchStartDate, LocalDate searchEndDate, Pageable pageable);
    Slice<ProductDto> findMyListProductDtoByProducts(User user, double longitude, double latitude, String soldStatus, Pageable pageable);
    Slice<ProductDto> findMySaveListProductDtoByProducts(User user, double longitude, double latitude, Pageable pageable);
    Slice<ProductDto> findOpponentListProductDtoByProducts(double longitude, double latitude, User user, User opponent, String soldStatus, Pageable pageable);
    Slice<ProductDto> searchProductByCondition(User user, String keyword, Double longitude, Double latitude, LocalDate searchStartDate, LocalDate searchEndDate, Pageable pageable);
}
