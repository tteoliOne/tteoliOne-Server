package store.tteolione.tteolione.domain.user.service;

import store.tteolione.tteolione.domain.user.entity.User;

public interface UserService {
    User findByUserId(Long userId);
}
