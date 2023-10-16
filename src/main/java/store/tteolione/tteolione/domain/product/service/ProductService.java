package store.tteolione.tteolione.domain.product.service;

import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.product.dto.PostProductRequest;

public interface ProductService {

    void saveProduct(MultipartFile images, PostProductRequest postProductRequest);
}
