package store.tteolione.tteolione.infra.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.tteolione.tteolione.domain.user.service.UserService;
import store.tteolione.tteolione.global.exception.GeneralException;
import store.tteolione.tteolione.infra.email.dto.SendEmailRequest;
import store.tteolione.tteolione.infra.email.dto.VerifyEmailRequest;
import store.tteolione.tteolione.infra.email.entity.EmailAuth;
import store.tteolione.tteolione.infra.email.repository.EmailAuthRepository;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;
    private final EmailAuthRepository emailAuthRepository;
    private String authCode;

    @Override
    public void sendEmailAuth(SendEmailRequest sendEmailRequest) throws Exception {
        userService.validateIsAlreadyRegisteredUser(sendEmailRequest.getEmail());
        try {
            MimeMessage message = createEmailContent(sendEmailRequest.getEmail());
            javaMailSender.send(message);
            EmailAuth emailAuth = EmailAuth.createEmailAuth(sendEmailRequest.getEmail(), authCode);
            emailAuthRepository.save(emailAuth);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verifyEmailCode(VerifyEmailRequest verifyEmailRequest) {
        EmailAuth emailAuth = emailAuthRepository.findByAuthCode(verifyEmailRequest.getAuthCode())
                .orElseThrow(() -> new GeneralException("인증코드를 찾을 수 없습니다."));
        LocalDateTime createAt = emailAuth.getCreateAt();
        LocalDateTime currentAt = LocalDateTime.now();
        if (createAt.until(currentAt, ChronoUnit.MINUTES) > 10) {
            throw new GeneralException("인증코드 유효한 시간인 10분을 초과했습니다.");
        }
        emailAuth.verifyEmail();
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

        int numIndex = random.nextInt(9999) + 1000;
        authCode += numIndex;

        return authCode;
    }
}
