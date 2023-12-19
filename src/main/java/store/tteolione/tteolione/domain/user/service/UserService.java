package store.tteolione.tteolione.domain.user.service;

import store.tteolione.tteolione.domain.user.dto.*;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.Optional;

public interface UserService {
    User findByUserId(Long userId);
    void validateIsAlreadyRegisteredUser(String email);
    ReissueResponse reissueToken(ReissueRequest reissueRequest);
    void signUpUser(SignUpRequest signUpRequest);
    void validateIsAlreadyRegisteredEmail(String email);

    LoginResponse loginUser(LoginRequest loginRequest);

    void duplicateNickname(String nickname);

    void duplicateLoginId(String loginId);

    User findByUsername(String loginId);

    String findLoginId(FindIdRequest findIdRequest) throws Exception;

    VerifyLoginIdResponse verifyLoginId(VerifyLoginIdRequest verifyLoginIdRequest);

    String findPassword(FindPasswordRequest findPasswordRequest) throws Exception;

    String verifyPassword(VerifyPasswordRequest verifyPasswordRequest);

    String resetPassword(ResetPasswordRequest resetPasswordRequest);

    void changeNickname(ChangeNicknameRequest changeNicknameRequest);
}
