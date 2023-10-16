package store.tteolione.tteolione.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.product.dto.PostProductRequest;
import store.tteolione.tteolione.domain.product.service.ProductService;
import store.tteolione.tteolione.global.dto.BaseResponse;
import store.tteolione.tteolione.global.service.S3Service;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final S3Service s3Service;

    @PostMapping
    public BaseResponse<Object> createProduct(@RequestPart(value = "images") MultipartFile images,
                                              @RequestPart(value = "request") PostProductRequest request) throws IOException {

        productService.saveProduct(images, request);
        return BaseResponse.of("A");
    }
}
