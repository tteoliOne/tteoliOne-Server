package store.tteolione.tteolione.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.tteolione.tteolione.domain.category.entity.Category;
import store.tteolione.tteolione.domain.category.repository.CategoryRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category findByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
