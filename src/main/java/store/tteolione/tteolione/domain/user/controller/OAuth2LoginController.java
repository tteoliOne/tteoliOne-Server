package store.tteolione.tteolione.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.tteolione.tteolione.domain.user.dto.LoginResponse;
import store.tteolione.tteolione.domain.user.dto.OAuth2KakaoRequest;
import store.tteolione.tteolione.domain.user.service.OAuth2LoginService;
import store.tteolione.tteolione.global.dto.BaseResponse;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class OAuth2LoginController {

    private final OAuth2LoginService oAuth2LoginService;

    /**
     * 카카오 로그인
     */
    @PostMapping("/kakao")
    public BaseResponse<LoginResponse> kakaoLogin(@RequestBody OAuth2KakaoRequest oAuth2KakaoRequest) {
        LoginResponse loginResponse = oAuth2LoginService.validateKakaoAccessToken(oAuth2KakaoRequest);
        return BaseResponse.of(loginResponse);
    }

}
