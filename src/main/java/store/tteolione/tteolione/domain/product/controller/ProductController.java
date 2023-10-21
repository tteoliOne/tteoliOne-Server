package store.tteolione.tteolione.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.product.dto.PostProductRequest;
import store.tteolione.tteolione.domain.product.dto.PostProductResponse;
import store.tteolione.tteolione.domain.product.service.ProductService;
import store.tteolione.tteolione.global.dto.BaseResponse;
import store.tteolione.tteolione.global.service.S3Service;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final S3Service s3Service;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<PostProductResponse> createProduct(@RequestPart(value = "photos") List<MultipartFile> photos,
                                                           @RequestPart(value = "receipt") MultipartFile receipt,
                                                           @RequestPart(value = "request") PostProductRequest request) throws IOException {

        PostProductResponse postProductResponse = productService.saveProduct(photos, receipt, request);
        return BaseResponse.of(postProductResponse);
    }
}
