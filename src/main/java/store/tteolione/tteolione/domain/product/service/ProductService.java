package store.tteolione.tteolione.domain.product.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.product.dto.*;
import store.tteolione.tteolione.domain.search.dto.SearchProductResponse;
import store.tteolione.tteolione.domain.user.entity.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ProductService {

    PostProductResponse saveProduct(List<MultipartFile> photos, MultipartFile receipt, PostProductRequest postProductRequest) throws IOException;

    GetSimpleProductResponse getSimpleProducts(double longitude, double latitude);

    Slice<ProductDto> getListProducts(Long categoryId, double longitude, double latitude, LocalDate searchStartDate, LocalDate searchEndDate, Pageable pageable);

    Slice<ProductDto> getMyListProducts(double longitude, double latitude, String soldStatus, Pageable pageable);

    Slice<ProductDto> getMySaveListProducts(double longitude, double latitude, Pageable pageable);

    Slice<ProductDto> getOpponentListProducts(double longitude, double latitude, Long opponentId, String soldStatus, Pageable pageable);

    String likeProduct(Long productId);

    PostSaveProductResponse savedList();

    DetailProductResponse detailProduct(Long productId);

    void deleteProduct(Long productId);

    PostProductResponse editProduct(Long productId, List<MultipartFile> photos, MultipartFile receipt, PostProductRequest request) throws IOException ;

    SearchProductResponse searchProductList(User user, String query, Double longitude, Double latitude, LocalDate searchStartDate, LocalDate searchEndDate, Pageable pageable);

    void requestProduct(Long productId);

    void approveProduct(Long productId, Long receiverId);

    void rejectProduct(Long productId, Long receiverId);

    void reviewProduct(Long productId, PostReviewRequest postReviewRequest);

}
