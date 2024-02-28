package store.tteolione.tteolione.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.product.repository.ProductRepository;
import store.tteolione.tteolione.domain.report.constant.ReportConstants;
import store.tteolione.tteolione.domain.report.dto.PostReportRequest;
import store.tteolione.tteolione.domain.report.entity.Report;
import store.tteolione.tteolione.domain.report.repository.ReportRepository;
import store.tteolione.tteolione.domain.room.entity.Chat;
import store.tteolione.tteolione.domain.room.repository.ChatRepository;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.service.UserService;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.exception.GeneralException;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final UserService userService;
    private final ReportRepository reportRepository;
    private final ProductRepository productRepository;
    private final ChatRepository chatRepository;

    @Override
    public void postReport(String reportType, Long id, String reportCategory, PostReportRequest postReportRequest) {
        if (!(reportType.equals("chat") || reportType.equals("products"))) {
            throw new GeneralException(Code.NOT_EXISTS_REPORT_TYPE);
        }

        if (!(reportCategory.equals("spam") || reportCategory.equals("image-violence") || reportCategory.equals("information") || reportCategory.equals("etc"))) {
            throw new GeneralException(Code.NOT_EXISTS_REPORT_CATEGORY);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByLoginId(authentication.getName());

        switch (reportType) {
            case "chat" -> saveChatReport(id, reportCategory, user, postReportRequest);
            case "products" -> saveProductReport(id, reportCategory, user, postReportRequest);
        }
    }

    //채팅방 신고
    private void saveChatReport(Long chatRoomId, String reportCategory, User reporter, PostReportRequest postReportRequest) {

        Chat chatRoom = chatRepository.findByChatId(chatRoomId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_CHAT_ROOM));
        User reportee = userService.findByUserId(postReportRequest.getReporteeId());

        ReportConstants.EReportCategory eReportCategory = getEReportCategory(reportCategory);

        Report report = postReportRequest.toChatReportEntity(chatRoom, eReportCategory, reporter, reportee);
        reportRepository.save(report);
    }

    //공유 상품 신고
    private void saveProductReport(Long productId, String reportCategory, User reporter, PostReportRequest postReportRequest) {

        Product product = productRepository.findByUserAndProductId(productId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_PRODUCT));

        ReportConstants.EReportCategory eReportCategory = getEReportCategory(reportCategory);

        Report report = postReportRequest.toProductReportEntity(product, eReportCategory, reporter);
        reportRepository.save(report);
    }

    private ReportConstants.EReportCategory getEReportCategory(String reportCategory) {
        ReportConstants.EReportCategory eReportCategory = null;
        switch (reportCategory) {
            case "spam" -> eReportCategory = ReportConstants.EReportCategory.eSpam;
            case "image-violence" -> eReportCategory = ReportConstants.EReportCategory.eImageAndVerbalAbuse;
            case "information" -> eReportCategory = ReportConstants.EReportCategory.eFalseInformation;
            case "etc" -> eReportCategory = ReportConstants.EReportCategory.eETC;
        }

        return eReportCategory;
    }
}
