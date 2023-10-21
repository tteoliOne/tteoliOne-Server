package store.tteolione.tteolione.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.category.entity.Category;
import store.tteolione.tteolione.domain.category.service.CategoryService;
import store.tteolione.tteolione.domain.file.entity.File;
import store.tteolione.tteolione.domain.file.service.FileService;
import store.tteolione.tteolione.domain.product.dto.PostProductRequest;
import store.tteolione.tteolione.domain.product.dto.PostProductResponse;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.product.repository.ProductRepository;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.service.UserService;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final FileService fileService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public PostProductResponse saveProduct(List<MultipartFile> photos, MultipartFile receipt, PostProductRequest postProductRequest) throws IOException {
        Product product = postProductRequest.toEntity();

        User findUser = userService.findByUserId(postProductRequest.getUserId());
        Category findCategory = categoryService.findByCategoryId(postProductRequest.getCategoryId());


        List<File> uploadPhotos = fileService.saveImages(photos);
        File uploadReceipt = fileService.saveImage(receipt);
        uploadPhotos.add(uploadReceipt);

        product.setCategory(findCategory);
        product.setUser(findUser);
        product.setImages(uploadPhotos);
        productRepository.save(product);

        return PostProductResponse.from(product);
    }
}
