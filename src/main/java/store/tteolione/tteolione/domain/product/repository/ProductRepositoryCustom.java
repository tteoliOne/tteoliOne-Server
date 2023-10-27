package store.tteolione.tteolione.domain.product.repository;

import store.tteolione.tteolione.domain.category.entity.Category;
import store.tteolione.tteolione.domain.product.dto.SimpleProductDto;

import java.util.List;

public interface ProductRepositoryCustom {

    List<SimpleProductDto> findSimpleDtoByProductsUserId(Long userId, Category category, double longitude, double latitude);
}
