package store.tteolione.tteolione.domain.category.service;

import store.tteolione.tteolione.domain.category.entity.Category;

public interface CategoryService {

    Category findByCategoryId(Long categoryId);
}
