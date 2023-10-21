package store.tteolione.tteolione.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
    }
}
