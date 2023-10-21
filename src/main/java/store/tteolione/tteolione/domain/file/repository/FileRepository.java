package store.tteolione.tteolione.domain.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.tteolione.tteolione.domain.file.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {
}
