package store.tteolione.tteolione.infra.email.service.v1;

import store.tteolione.tteolione.infra.email.dto.v1.VerifyEmailRequest;

public interface EmailService {
    void sendEmailAuth(String email) throws Exception;
    boolean verifyEmailCode(VerifyEmailRequest verifyEmailRequest);

}
