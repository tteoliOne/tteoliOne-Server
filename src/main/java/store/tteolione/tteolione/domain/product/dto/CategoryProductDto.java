package store.tteolione.tteolione.domain.product.dto;

import lombok.Data;
import store.tteolione.tteolione.domain.category.entity.Category;

import java.util.List;

@Data
public class CategoryProductDto {
    private Long categoryId;
    private String categoryName;
    private List<ProductDto> products; //카테고리별 상품목록

    public CategoryProductDto(Category category, List<ProductDto> products) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.products = products;
    }
}
