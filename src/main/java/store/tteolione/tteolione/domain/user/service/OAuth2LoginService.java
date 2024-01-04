package store.tteolione.tteolione.domain.user.service;

import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.user.dto.LoginResponse;
import store.tteolione.tteolione.domain.user.dto.OAuth2KakaoRequest;

import java.io.IOException;

public interface OAuth2LoginService {
    LoginResponse validateKakaoAccessToken(OAuth2KakaoRequest oAuth2KakaoRequest);

    LoginResponse signUpKakao(MultipartFile profile, OAuth2KakaoRequest oAuth2KakaoRequest) throws IOException;
}
