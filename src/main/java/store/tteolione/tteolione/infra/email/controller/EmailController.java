package store.tteolione.tteolione.infra.email.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.tteolione.tteolione.domain.user.service.UserService;
import store.tteolione.tteolione.global.dto.BaseResponse;
import store.tteolione.tteolione.infra.email.dto.SendEmailRequest;
import store.tteolione.tteolione.infra.email.dto.VerifyEmailRequest;
import store.tteolione.tteolione.infra.email.service.EmailService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;

    @PostMapping("/send/signup")
    public BaseResponse<String> sendEmailAuth(@Valid @RequestBody SendEmailRequest sendEmailRequest) throws Exception {
        userService.validateIsAlreadyRegisteredUser(sendEmailRequest.getEmail());
        emailService.sendEmailAuth(sendEmailRequest.getEmail());
        return BaseResponse.of("이메일 인증코드 발송에 성공했습니다.");
    }

    @PostMapping("/verify/signup")
    public BaseResponse<String> verifyEmailCode(@Valid @RequestBody VerifyEmailRequest verifyEmailRequest) {
        emailService.verifyEmailCode(verifyEmailRequest);
        return BaseResponse.of("이메일 인증 성공했습니다.");
    }
}
