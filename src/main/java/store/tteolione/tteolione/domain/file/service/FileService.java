package store.tteolione.tteolione.domain.file.service;

import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.file.entity.File;
import store.tteolione.tteolione.domain.product.entity.Product;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<File> saveImages(Product product, List<MultipartFile> multipartFiles) throws IOException;
}
