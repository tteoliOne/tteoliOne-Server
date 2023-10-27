package store.tteolione.tteolione.domain.category.service;

import store.tteolione.tteolione.domain.category.entity.Category;

import java.util.List;

public interface CategoryService {

    Category findByCategoryId(Long categoryId);

    List<Category> findAll();
}
