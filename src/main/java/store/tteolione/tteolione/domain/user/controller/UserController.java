package store.tteolione.tteolione.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
     * 닉네임 중복 확인
     */
    @PostMapping("/check/nickname")
    public BaseResponse<String> duplicateNickname(@Valid @RequestBody DupNicknameRequest dupNicknameRequest) {
        userService.duplicateNickname(dupNicknameRequest.getNickname());
        return BaseResponse.of("사용가능한 닉네임입니다.");
    }

    /**
     * 아이디 중복 확인
     */
    @PostMapping("/check/login-id")
    public BaseResponse<String> duplicateLoginId(@Valid @RequestBody DupLoginIdRequest dupLoginIdRequest) {
        userService.duplicateLoginId(dupLoginIdRequest.getLoginId());
        return BaseResponse.of("사용가능한 아이디입니다.");
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
