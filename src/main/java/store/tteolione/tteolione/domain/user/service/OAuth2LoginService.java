package store.tteolione.tteolione.domain.user.service;

import store.tteolione.tteolione.domain.user.dto.LoginResponse;
import store.tteolione.tteolione.domain.user.dto.OAuth2KakaoRequest;

public interface OAuth2LoginService {
    LoginResponse validateKakaoAccessToken(OAuth2KakaoRequest oAuth2KakaoRequest);
}
