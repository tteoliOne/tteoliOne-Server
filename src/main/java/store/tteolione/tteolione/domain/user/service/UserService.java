package store.tteolione.tteolione.domain.user.service;

import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.user.dto.*;
import store.tteolione.tteolione.domain.user.entity.User;

import java.io.IOException;

public interface UserService {
    User findByUserId(Long userId);
    void validateIsAlreadyRegisteredUser(String email);
    ReissueResponse reissueToken(ReissueRequest reissueRequest);
    void signUpUser(SignUpRequest signUpRequest, MultipartFile profile) throws IOException;
    void validateIsAlreadyRegisteredEmail(String email);

    LoginResponse loginUser(LoginRequest loginRequest);

    void logout();

    void duplicateNickname(String nickname);

    void duplicateLoginId(String loginId);

    User findByLoginId(String loginId);

    String findLoginId(FindIdRequest findIdRequest) throws Exception;

    VerifyLoginIdResponse verifyLoginId(VerifyLoginIdRequest verifyLoginIdRequest);

    String findPassword(FindPasswordRequest findPasswordRequest) throws Exception;

    String verifyPassword(VerifyPasswordRequest verifyPasswordRequest);

    String resetPassword(ResetPasswordRequest resetPasswordRequest);

    void changeNickname(ChangeNicknameRequest changeNicknameRequest);

    void editUserInfo(EditUserInfoRequest editUserInfoRequest, MultipartFile profile) throws IOException;

    GetUserInfoResponse getUserInfo();

    SimpleProfileResponse getSimpleProfile(Long userId);

    void deleteUser(Long userId, OAuth2RevokeRequest oAuth2RevokeRequest);
}
