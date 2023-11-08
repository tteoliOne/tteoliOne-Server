package store.tteolione.tteolione.domain.user.service;

import store.tteolione.tteolione.domain.user.dto.ReissueRequest;
import store.tteolione.tteolione.domain.user.dto.ReissueResponse;
import store.tteolione.tteolione.domain.user.dto.SignUpRequest;
import store.tteolione.tteolione.domain.user.entity.User;

public interface UserService {
    User findByUserId(Long userId);
    void validateIsAlreadyRegisteredUser(String email);
    ReissueResponse reissueToken(ReissueRequest reissueRequest);
    void signUpUser(SignUpRequest signUpRequest);
    void validateIsAlreadyRegisteredEmail(String email);
}
