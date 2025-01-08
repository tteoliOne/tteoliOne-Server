package store.tteolione.tteolione.infra.email.controller.v2;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.tteolione.tteolione.global.config.redis.RedisUtil;
import store.tteolione.tteolione.global.dto.BaseResponse;
import store.tteolione.tteolione.infra.email.dto.v2.EmailAuthCodeReq;
import store.tteolione.tteolione.infra.email.dto.v2.EmailSendReq;
import store.tteolione.tteolione.infra.email.service.v2.EmailServiceV2;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/email")
public class EmailControllerV2 {

    private final EmailServiceV2 emailService;
    private final RedisUtil redisUtil;

    @PostMapping("/send")
    public BaseResponse<String> sendEmail(@Valid @RequestBody EmailSendReq request) throws MessagingException {
        emailService.validateExistUser(request.getEmail());
        boolean result = emailService.sendEmail(request.getEmail());
        if (result) {
            return BaseResponse.of("이메일 인증코드 발송에 성공했습니다.");
        }
        return BaseResponse.of("이메일 인증코드 발송에 실패했습니다.");
    }

    @PostMapping("/verify")
    public BaseResponse<String> verifyEmailAndCode(@Valid @RequestBody EmailAuthCodeReq request) {
        String codeFoundByEmail = redisUtil.getData("code:"+request.getEmail());
        emailService.verifyEmailCode(request.getEmail(), request.getCode(), codeFoundByEmail);
        return BaseResponse.of("이메일 인증 성공했습니다.");
    }
}