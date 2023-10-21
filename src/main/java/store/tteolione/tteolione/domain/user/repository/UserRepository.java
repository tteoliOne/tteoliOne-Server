package store.tteolione.tteolione.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.tteolione.tteolione.domain.user.constant.UserConstants;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndLoginType(String email, UserConstants.ELoginType loginType);
    Optional<User> findByUserId(Long userId);
    boolean existsByEmailAndLoginType(String email, UserConstants.ELoginType loginType);
}
