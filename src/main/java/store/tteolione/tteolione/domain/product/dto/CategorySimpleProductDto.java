package store.tteolione.tteolione.domain.product.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import store.tteolione.tteolione.domain.category.entity.Category;

import java.util.List;

@Data
public class CategorySimpleProductDto {
    private Long categoryId;
    private String categoryName;
    private List<SimpleProductDto> products;

    public CategorySimpleProductDto(Category category, List<SimpleProductDto> products) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.products = products;
    }
}
