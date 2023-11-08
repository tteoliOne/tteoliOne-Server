package store.tteolione.tteolione.infra.email.service;

import store.tteolione.tteolione.infra.email.dto.SendEmailRequest;
import store.tteolione.tteolione.infra.email.dto.VerifyEmailRequest;

public interface EmailService {
    void sendEmailAuth(SendEmailRequest sendEmailRequest) throws Exception;
    void verifyEmailCode(VerifyEmailRequest verifyEmailRequest);
}
