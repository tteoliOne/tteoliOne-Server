package store.tteolione.tteolione.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.tteolione.tteolione.domain.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
