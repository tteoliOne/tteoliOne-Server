package store.tteolione.tteolione.domain.report.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.tteolione.tteolione.domain.report.dto.PostReportRequest;
import store.tteolione.tteolione.domain.report.service.ReportService;
import store.tteolione.tteolione.global.dto.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{reportType}/{id}")
    public BaseResponse<String> postReport(@PathVariable String reportType, @PathVariable Long id, @RequestParam("reportCategory") String reportCategory, @RequestBody PostReportRequest postReportRequest) {
        reportService.postReport(reportType, id, reportCategory, postReportRequest);
        return BaseResponse.of("정상적으로 신고 작성되었습니다.");
    }
}
