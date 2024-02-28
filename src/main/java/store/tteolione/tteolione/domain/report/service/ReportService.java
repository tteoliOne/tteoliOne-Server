package store.tteolione.tteolione.domain.report.service;

import store.tteolione.tteolione.domain.report.dto.PostReportRequest;

public interface ReportService {
    void postReport(String reportType, Long id, String reportCategory, PostReportRequest postReportRequest);
}
