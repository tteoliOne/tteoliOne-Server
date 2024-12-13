package store.tteolione.tteolione.infra.email.service.v2;

import jakarta.mail.MessagingException;

public interface EmailServiceV2 {

    boolean sendEmail(String toEmail) throws MessagingException;

    boolean verifyEmailCode(String email, String code, String codeFoundByEmail);

}
