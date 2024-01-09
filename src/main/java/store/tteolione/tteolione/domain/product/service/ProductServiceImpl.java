package store.tteolione.tteolione.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
import store.tteolione.tteolione.domain.product.dto.*;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.product.repository.ProductRepository;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.service.UserService;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.exception.GeneralException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User findUser = userService.findByUsername(authentication.getName());
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
    public GetSimpleProductResponse getSimpleProducts(double longitude, double latitude) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());

        List<CategoryProductDto> categoryProductDtos = categoryService.findAll().stream()
                .map(category -> {
                    List<ProductDto> productDtos = productRepository.findSimpleDtoByProductsUserId(user, category, longitude, latitude);
                    return new CategoryProductDto(category, productDtos);
                })
                .collect(Collectors.toList());

        return GetSimpleProductResponse.builder()
                .list(categoryProductDtos)
                .build();
    }

    @Override
    public Slice<ProductDto> getListProducts(Long categoryId, double longitude, double latitude, LocalDate searchStartDate, LocalDate searchEndDate, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());

        Category category = categoryService.findByCategoryId(categoryId);

        Slice<ProductDto> listProductDtoByProducts = productRepository.findListProductDtoByProducts(category, user, longitude, latitude, searchStartDate, searchEndDate, pageable);

        return listProductDtoByProducts;
    }

    @Override
    public Slice<ProductDto> getMyListProducts(double longitude, double latitude, String soldStatus, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());

        Slice<ProductDto> listProductDtoByProducts = productRepository.findMyListProductDtoByProducts(user, longitude, latitude, soldStatus, pageable);

        return listProductDtoByProducts;
    }

    @Override
    public String likeProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_PRODUCT));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        Optional<Likes> _findLikes = likesService.findByProductAndUser(product, user);

        if (_findLikes.isEmpty()) {
            //좋아요를 누른적 없다면 likes 생성후, 좋아요 처리
            Likes likes = Likes.toEntity(user, product);
            likesService.createLikes(likes);
            product.likeProduct(likes);
            return "좋아요 추가 성공";
        } else {
            //좋아요 누른적 있다면 취소 처리 후 데이터 삭제
            Likes findLikes = _findLikes.get();
            product.unLikeProduct(findLikes);
            likesService.deleteLikes(findLikes);
            return "좋아요 취소 성공";
        }
    }

    @Override
    public PostSaveProductResponse savedList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        List<Likes> likes = likesService.savedProducts(user);
        PostSaveProductResponse data = PostSaveProductResponse.toData(likes);
        return data;
    }

    @Override
    public DetailProductResponse detailProduct(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User buyer = userService.findByUsername(authentication.getName());
        Product detailProduct = productRepository.findByDetailProduct(productId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_PRODUCT));
        List<String> productImages = detailProduct.productImages();
        String receiptImage = detailProduct.receiptImage();
        Likes likes = likesService.findByProductAndUser(detailProduct, buyer).orElse(null);
        DetailProductResponse data = DetailProductResponse.toData(detailProduct, buyer, productImages, receiptImage, likes);
        return data;
    }

    @Override
    public void deleteProduct(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        Product product = productRepository.findByDetailProduct(productId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_PRODUCT));
        if (!user.getUserId().equals(product.getUser().getUserId())) {
            throw new GeneralException(Code.MATCH_PRODUCT_USER);
        }
        product.setStatus("DELETE");
    }

    @Override
    public PostProductResponse editProduct(Long productId, List<MultipartFile> photos, MultipartFile receipt, PostProductRequest postProductRequest) throws IOException  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());

        Product product = productRepository.findByDetailProduct(productId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_PRODUCT));

        if (!user.getUserId().equals(product.getUser().getUserId())) {
            throw new GeneralException(Code.MATCH_PRODUCT_USER);
        }

        //상품과 관련된 사진 제거
        fileService.deleteByImages(product);
        product.setImages(null);

        product.updateEntity(postProductRequest);

        Category findCategory = categoryService.findByCategoryId(postProductRequest.getCategoryId());

        //상품 이미지 업로드
        List<File> uploadPhotos = fileService.saveImages(photos);
        for (File uploadPhoto : uploadPhotos) {
            uploadPhoto.setProduct(product);
        }
        //영수증 이미지 업로드
        File uploadReceipt = fileService.saveImage(receipt);
        uploadReceipt.setProduct(product);
        uploadPhotos.add(uploadReceipt);

        //카테고리 최신화
        product.setCategory(findCategory);
        //이미지 최신화
        product.setImages(uploadPhotos);
        product.setUser(user);
//        productRepository.save(product);

        return PostProductResponse.from(product);
    }
}
