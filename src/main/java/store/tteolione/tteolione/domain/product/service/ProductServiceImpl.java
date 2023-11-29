package store.tteolione.tteolione.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.category.entity.Category;
import store.tteolione.tteolione.domain.category.service.CategoryService;
import store.tteolione.tteolione.domain.file.entity.File;
import store.tteolione.tteolione.domain.file.service.FileService;
import store.tteolione.tteolione.domain.likes.entity.Likes;
import store.tteolione.tteolione.domain.likes.service.LikesService;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;
import store.tteolione.tteolione.domain.product.dto.*;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.product.repository.ProductRepository;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.service.UserService;
import store.tteolione.tteolione.global.exception.GeneralException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final FileService fileService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final LikesService likesService;
    private final ProductRepository productRepository;

    @Override
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

    @Override
    public String likeProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new GeneralException("존재하지 않은 상품입니다."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        Optional<Likes> _findLikes = likesService.findByProductAndUser(product, user);

        if (!_findLikes.isPresent()) {
            //좋아요를 누른적 없다면 likes 생성후, 좋아요 처리
            product.setLikeCount(product.getLikeCount() + 1);
            Likes likes = Likes.toEntity(user, product);
            likesService.createLikes(likes);
            return "좋아요 추가 성공";
        } else {
            //좋아요 누른적 있다면 취소 처리 후 데이터 삭제
            Likes findLikes = _findLikes.get();
            findLikes.unLikeProduct(product);
            likesService.deleteLikes(findLikes);
            return "좋아요 취소 성공";
        }
    }
}
