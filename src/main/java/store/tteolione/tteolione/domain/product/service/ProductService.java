package store.tteolione.tteolione.domain.product.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.product.dto.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ProductService {

    PostProductResponse saveProduct(List<MultipartFile> photos, MultipartFile receipt, PostProductRequest postProductRequest) throws IOException;

    GetSimpleProductResponse getSimpleProducts(double longitude, double latitude);

    Slice<ProductDto> getListProducts(Long categoryId, double longitude, double latitude, LocalDate searchStartDate, LocalDate searchEndDate, Pageable pageable);
    Slice<ProductDto> getMyListProducts(double longitude, double latitude, String soldStatus, Pageable pageable);

    String likeProduct(Long productId);

    PostSaveProductResponse savedList();

    DetailProductResponse detailProduct(Long productId);

    void deleteProduct(Long productId);
}
