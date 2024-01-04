package store.tteolione.tteolione.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.user.dto.*;
import store.tteolione.tteolione.domain.user.service.UserService;
import store.tteolione.tteolione.global.dto.BaseResponse;

import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping(path = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<String> signupUser(@Valid @RequestPart(value = "signUpRequest") SignUpRequest signUpRequest,
                                           @RequestPart(value = "profile") MultipartFile profile) throws IOException {
        userService.signUpUser(signUpRequest, profile);
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
     * 아이디 찾기
     */
    @PostMapping("/find/login-id")
    public BaseResponse<String> findLoginId(@Valid @RequestBody FindIdRequest findIdRequest) throws Exception {
        return BaseResponse.of(userService.findLoginId(findIdRequest));
    }

    /**
     * 아이디 이메일 검증
     */
    @PostMapping("/verify/login-id")
    public BaseResponse<VerifyLoginIdResponse> findLoginId(@Valid @RequestBody VerifyLoginIdRequest verifyLoginIdRequest) throws Exception {
        return BaseResponse.of(userService.verifyLoginId(verifyLoginIdRequest));
    }

    /**
     * 비밀번호 찾기
     */
    @PostMapping("/find/password")
    public BaseResponse<String> findPassword(@Valid @RequestBody FindPasswordRequest findPasswordRequest) throws Exception {
        return BaseResponse.of(userService.findPassword(findPasswordRequest));
    }

    /**
     * 비밀번호 이메일 검증
     */
    @PostMapping("/verify/password")
    public BaseResponse<String> verifyPassword(@Valid @RequestBody VerifyPasswordRequest verifyPasswordRequest) {
        return BaseResponse.of(userService.verifyPassword(verifyPasswordRequest));
    }

    /**
     * 비밀번호 재설정
     */
    @PatchMapping("/reset/password")
    public BaseResponse<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return BaseResponse.of(userService.resetPassword(resetPasswordRequest));
    }

    /**
     * 닉네임 변경
     */
    @PatchMapping("/nickname")
    public BaseResponse<String> changeNickname(@Valid @RequestBody ChangeNicknameRequest changeNicknameRequest) {
        userService.changeNickname(changeNicknameRequest);
        return BaseResponse.of("정상적으로 닉네임이 변경되었습니다.");
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
