package store.tteolione.tteolione.infra.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.exception.GeneralException;
import store.tteolione.tteolione.infra.email.dto.VerifyEmailRequest;
import store.tteolione.tteolione.infra.email.entity.EmailAuth;
import store.tteolione.tteolione.infra.email.repository.EmailAuthRepository;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailAuthRepository emailAuthRepository;
    private String authCode;

    @Override
    public void sendEmailAuth(String email) throws Exception {
        Optional<EmailAuth> _findEmailAuth = emailAuthRepository.findByEmail(email);
        if (_findEmailAuth.isPresent()) {
            //이메일이 존재한다면 제거
            EmailAuth findEmailAuth = _findEmailAuth.get();
            User findUser = findEmailAuth.getUser();
            if (findUser != null) {
                findUser.setEmailAuth(null);
            }
            emailAuthRepository.delete(findEmailAuth);
        }
        try {
            MimeMessage message = createEmailContent(email);
            javaMailSender.send(message);
            EmailAuth emailAuth = EmailAuth.createEmailAuth(email, authCode);
            emailAuthRepository.save(emailAuth);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifyEmailCode(VerifyEmailRequest verifyEmailRequest) {
        EmailAuth emailAuth = emailAuthRepository.findByEmailAndAuthCode(verifyEmailRequest.getEmail(), verifyEmailRequest.getAuthCode())
                .orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_AUTHCODE));
        LocalDateTime createAt = emailAuth.getCreateAt();
        LocalDateTime currentAt = LocalDateTime.now();
        if (createAt.until(currentAt, ChronoUnit.MINUTES) > 10) {
            throw new GeneralException(Code.VALIDATION_AUTHCODE);
        }
        emailAuth.verifyEmail();
        return true;
    }

    private MimeMessage createEmailContent(String email) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("떠리원 회원가입 이메일 인증");

        authCode = createAuthCode();

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1> 안녕하세요 떠리원입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += authCode + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("tteolione@gmail.com", "떠리원"));//보내는 사람

        return message;
    }

    private String createAuthCode() {
        Random random = new Random();
        String authCode = "";

        for (int i = 0; i < 3; i++) {
            int index = random.nextInt(25) + 65;
            authCode += (char) index;
        }

        int numIndex = random.nextInt(9000) + 1000;
        authCode += numIndex;

        return authCode;
    }
}
