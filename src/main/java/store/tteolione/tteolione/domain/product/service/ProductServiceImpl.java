package store.tteolione.tteolione.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.category.entity.Category;
import store.tteolione.tteolione.domain.category.service.CategoryService;
import store.tteolione.tteolione.domain.file.entity.File;
import store.tteolione.tteolione.domain.file.service.FileService;
import store.tteolione.tteolione.domain.likes.service.LikesService;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;
import store.tteolione.tteolione.domain.product.dto.*;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.product.repository.ProductRepository;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final FileService fileService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final LikesService likesService;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public PostProductResponse saveProduct(List<MultipartFile> photos, MultipartFile receipt, PostProductRequest postProductRequest) throws IOException {
        Product product = postProductRequest.toEntity();

        User findUser = userService.findByUserId(postProductRequest.getUserId());
        Category findCategory = categoryService.findByCategoryId(postProductRequest.getCategoryId());


        List<File> uploadPhotos = fileService.saveImages(photos);
        for (File uploadPhoto : uploadPhotos) {
            uploadPhoto.setProduct(product);
        }
        File uploadReceipt = fileService.saveImage(receipt);
        uploadReceipt.setProduct(product);
        uploadPhotos.add(uploadReceipt);

        product.setCategory(findCategory);
        product.setUser(findUser);
        product.setImages(uploadPhotos);
        productRepository.save(product);

        return PostProductResponse.from(product);
    }

    @Override
    public GetSimpleProductResponse getSimpleProducts(Long userId, double longitude, double latitude) {
        List<CategorySimpleProductDto> categorySimpleProductDtos = new ArrayList<>();
        List<Category> allCategory = categoryService.findAll(); //카테고리 id 전부가져오고
        for (Category category : allCategory) {
            List<SimpleProductDto> simpleDtoByProductsUserId = productRepository.findSimpleDtoByProductsUserId(userId, category, longitude, latitude);
            categorySimpleProductDtos.add(new CategorySimpleProductDto(category, simpleDtoByProductsUserId));
        }

        return GetSimpleProductResponse.builder()
                .list(categorySimpleProductDtos)
                .build();
    }
}
