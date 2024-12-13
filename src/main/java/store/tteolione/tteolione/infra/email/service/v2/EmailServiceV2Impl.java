package store.tteolione.tteolione.infra.email.service.v2;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.tteolione.tteolione.global.config.email.EmailSendClient;
import store.tteolione.tteolione.global.config.redis.RedisUtil;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.exception.GeneralException;
import store.tteolione.tteolione.infra.email.entity.EmailAuth;
import store.tteolione.tteolione.infra.email.repository.EmailAuthRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailServiceV2Impl implements EmailServiceV2{

    private final EmailSendClient emailSendClient;
    private final EmailAuthRepository emailAuthRepository;
    private final RedisUtil redisUtil;

    @Override
    public boolean sendEmail(String toEmail) throws MessagingException {
        if (redisUtil.existData("code:"+toEmail)) {
            redisUtil.deleteData("code:"+toEmail);
        }
        return emailSendClient.sendEmail(toEmail);
    }

    @Override
    @Transactional
    public boolean verifyEmailCode(String email, String code, String codeFoundByEmail) {
        log.info("Redis 인증코드 값 = {}", codeFoundByEmail);

        if (codeFoundByEmail == null) {
            throw new GeneralException(Code.VALIDATION_AUTHCODE);
        }
        boolean verifySuccess = codeFoundByEmail.equals(code);
        if (verifySuccess) {
            emailAuthRepository.save(EmailAuth.createEmailAuth(email));
            redisUtil.deleteData("code:"+email);
            return true;
        } else {
            throw new GeneralException(Code.NOT_EXISTS_AUTHCODE);
        }
    }
}
