package store.tteolione.tteolione.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.product.dto.*;
import store.tteolione.tteolione.domain.product.service.ProductService;
import store.tteolione.tteolione.global.dto.BaseResponse;
import store.tteolione.tteolione.global.service.S3Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final S3Service s3Service;

    /**
     * 상품 등록
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<PostProductResponse> createProduct(@RequestPart(value = "photos") List<MultipartFile> photos,
                                                           @RequestPart(value = "receipt") MultipartFile receipt,
                                                           @RequestPart(value = "request") PostProductRequest request) throws IOException {

        PostProductResponse postProductResponse = productService.saveProduct(photos, receipt, request);
        return BaseResponse.of(postProductResponse);
    }

    /**
     * 상품 간단 조회
     */
    @GetMapping("/simple")
    public BaseResponse<GetSimpleProductResponse> simpleProducts(@RequestParam Double longitude,
                                                                 @RequestParam Double latitude) {
        GetSimpleProductResponse simpleProductResponse = productService.getSimpleProducts(longitude, latitude);
        return BaseResponse.of(simpleProductResponse);
    }

    /**
     * 상품 목록 조회
     */
    @GetMapping()
    public BaseResponse<Slice<ProductDto>> getListProducts(@RequestParam Long categoryId,
                                                          @RequestParam Double longitude,
                                                          @RequestParam Double latitude,
                                                          @RequestParam(required = false, defaultValue = "19000101") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate searchStartDate,
                                                          @RequestParam(required = false, defaultValue = "99991231") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate searchEndDate,
                                                          Pageable pageable
    ) {
        Slice<ProductDto> simpleProductResponse = productService.getListProducts(categoryId, longitude, latitude, searchStartDate, searchEndDate, pageable);
        return BaseResponse.of(simpleProductResponse);
    }

    /**
     * 좋아요 추가 취소
     */
    @PostMapping("/{productId}/likes")
    public BaseResponse<String> likeProduct(@PathVariable Long productId) {
        String result = productService.likeProduct(productId);
        return BaseResponse.of(result);
    }

    /**
     * 저장 목록 조회
     */
    @GetMapping("/saved")
    public BaseResponse<PostSaveProductResponse> savedProducts() {
        PostSaveProductResponse postSaveProductResponse = productService.savedList();
        return BaseResponse.of(postSaveProductResponse);
    }

    /**
     * 상세 목록 조회
     */
    @GetMapping("/{productId}")
    public BaseResponse<DetailProductResponse> detailProduct(@PathVariable Long productId) {

        return BaseResponse.of(productService.detailProduct(productId));
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{productId}")
    public BaseResponse<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return BaseResponse.of("정상적으로 삭제되었습니다.");
    }

    /**
     * 내 공유글 목록
     */
    @GetMapping("/me")
    public BaseResponse<Slice<ProductDto>> getMyListProducts(@RequestParam Double longitude,
                                                           @RequestParam Double latitude,
                                                           @RequestParam(required = false) String status,
                                                           Pageable pageable
    ) {
        Slice<ProductDto> simpleProductResponse = productService.getMyListProducts(longitude, latitude, status, pageable);
        return BaseResponse.of(simpleProductResponse);
    }
}
