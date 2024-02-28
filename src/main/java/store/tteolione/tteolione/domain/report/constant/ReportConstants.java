package store.tteolione.tteolione.domain.report.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ReportConstants {

    @Getter
    @AllArgsConstructor
    public enum EReportCategory {
        eSpam("스팸 신고"),
        eImageAndVerbalAbuse("이미지 및 언어폭력"),
        eFalseInformation("거짓 정보"),
        eETC("기타");
        private final String name;
    }

    @Getter
    @AllArgsConstructor
    public enum EReportType {
        eChat("채팅 신고"),
        eProduct("공유 상품 신고");
        private final String name;
    }

}
