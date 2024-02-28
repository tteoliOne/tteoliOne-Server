package store.tteolione.tteolione.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.report.entity.Report;
import store.tteolione.tteolione.domain.room.entity.Chat;
import store.tteolione.tteolione.domain.user.entity.User;

import static store.tteolione.tteolione.domain.report.constant.ReportConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostReportRequest {

    private String content; //기타일때만
    private Long reporteeId; //채팅신고일때

    public Report toChatReportEntity(Chat chat, EReportCategory reportCategory, User reporter, User reportee) {
        return Report.builder()
                .reportType(EReportType.eChat)
                .reportCategory(reportCategory)
                .chat(chat)
                .reporter(reporter)
                .reportee(reportee)
                .content(content)
                .build();
    }

    public Report toProductReportEntity(Product product, EReportCategory reportCategory, User reporter) {
        return Report.builder()
                .reportType(EReportType.eProduct)
                .reportCategory(reportCategory)
                .product(product)
                .reporter(reporter)
                .reportee(product.getUser())
                .content(content)
                .build();
    }
}
