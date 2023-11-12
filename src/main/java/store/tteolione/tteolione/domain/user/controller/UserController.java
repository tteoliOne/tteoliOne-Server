package store.tteolione.tteolione.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.tteolione.tteolione.domain.user.dto.*;
import store.tteolione.tteolione.domain.user.service.UserService;
import store.tteolione.tteolione.global.dto.BaseResponse;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public BaseResponse<String> signupUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        userService.signUpUser(signUpRequest);
        return BaseResponse.of("회원가입 성공입니다.");
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public BaseResponse<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.loginUser(loginRequest);
        return BaseResponse.of(loginResponse);
    }



    /**
     * 토큰 재발행
     */
    @PostMapping("/reissue")
    public BaseResponse<ReissueResponse> reissue(@Valid @RequestBody ReissueRequest reissueRequest) {
        ReissueResponse reissueResponse = userService.reissueToken(reissueRequest);
        return BaseResponse.of(reissueResponse);
    }
}
