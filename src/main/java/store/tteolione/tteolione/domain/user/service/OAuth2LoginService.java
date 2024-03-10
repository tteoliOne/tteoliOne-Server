package store.tteolione.tteolione.domain.user.service;

import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.user.dto.LoginResponse;
import store.tteolione.tteolione.domain.user.dto.apple.AppleIdTokenPayload;
import store.tteolione.tteolione.domain.user.dto.apple.OAuth2AppleRequest;
import store.tteolione.tteolione.domain.user.dto.kakao.OAuth2KakaoRequest;

import java.io.IOException;

public interface OAuth2LoginService {
    LoginResponse validateKakaoAccessToken(OAuth2KakaoRequest oAuth2KakaoRequest);

    LoginResponse signUpKakao(MultipartFile profile, OAuth2KakaoRequest oAuth2KakaoRequest) throws IOException;

    void kakaoRevoke(String providerId);

    LoginResponse validateAppleIdToken(OAuth2AppleRequest oAuth2AppleRequest);

    LoginResponse signUpApple(MultipartFile profile, OAuth2AppleRequest oAuth2AppleRequest) throws IOException;

    void appleRevoke(String authorization);
}
