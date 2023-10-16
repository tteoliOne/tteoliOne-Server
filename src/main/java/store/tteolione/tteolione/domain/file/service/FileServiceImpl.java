package store.tteolione.tteolione.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.file.entity.File;
import store.tteolione.tteolione.domain.file.repository.FileRepository;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.global.service.S3Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Service s3Service;
    private final FileRepository fileRepository;

    @Override
    @Transactional
    public List<File> saveImages(Product product, List<MultipartFile> multipartFiles) throws IOException {
        List<File> uploadFiles = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String uploadUrl = s3Service.uploadFile(multipartFile);
            File saveImage = fileRepository.save(File.toEntity(uploadUrl, product));
            uploadFiles.add(saveImage);
        }

        return uploadFiles;
    }

}
