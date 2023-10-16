package store.tteolione.tteolione.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.file.service.FileService;
import store.tteolione.tteolione.domain.product.dto.PostProductRequest;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final FileService fileService;

    @Override
    @Transactional
    public void saveProduct(MultipartFile images, PostProductRequest postProductRequest) {

    }
}
