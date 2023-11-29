package store.tteolione.tteolione.domain.product.service;

import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.product.dto.GetSimpleProductResponse;
import store.tteolione.tteolione.domain.product.dto.PostProductRequest;
import store.tteolione.tteolione.domain.product.dto.PostProductResponse;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    PostProductResponse saveProduct(List<MultipartFile> photos, MultipartFile receipt, PostProductRequest postProductRequest) throws IOException;

    GetSimpleProductResponse getSimpleProducts(Long userId, double longitude, double latitude);

    String likeProduct(Long productId);
}
