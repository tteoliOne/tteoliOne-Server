package store.tteolione.tteolione.domain.user.service;

import store.tteolione.tteolione.domain.user.dto.*;
import store.tteolione.tteolione.domain.user.entity.User;

public interface UserService {
    User findByUserId(Long userId);
    void validateIsAlreadyRegisteredUser(String email);
    ReissueResponse reissueToken(ReissueRequest reissueRequest);
    void signUpUser(SignUpRequest signUpRequest);
    void validateIsAlreadyRegisteredEmail(String email);

    LoginResponse loginUser(LoginRequest loginRequest);

    void duplicateNickname(String nickname);

    void duplicateLoginId(String loginId);
}
