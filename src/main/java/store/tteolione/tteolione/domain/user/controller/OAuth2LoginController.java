package store.tteolione.tteolione.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.user.dto.LoginResponse;
import store.tteolione.tteolione.domain.user.dto.OAuth2KakaoRequest;
import store.tteolione.tteolione.domain.user.service.OAuth2LoginService;
import store.tteolione.tteolione.global.dto.BaseResponse;

import java.io.IOException;


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

    /**
     * 카카오 프로필 설정
     */
    @PostMapping(path = "/kakao/profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<LoginResponse> kakaoSignup(@RequestPart(value = "profile") MultipartFile profile,
                                                   @RequestPart(value = "oAuth2KakaoRequest") OAuth2KakaoRequest oAuth2KakaoRequest) throws IOException {
        LoginResponse loginResponse = oAuth2LoginService.signUpKakao(profile, oAuth2KakaoRequest);
        return BaseResponse.of(loginResponse);
    }

}
