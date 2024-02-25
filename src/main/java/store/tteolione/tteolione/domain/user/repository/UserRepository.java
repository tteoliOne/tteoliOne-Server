package store.tteolione.tteolione.domain.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.tteolione.tteolione.domain.user.constant.UserConstants;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLoginId(String loginId);
    Optional<User> findByEmailAndLoginType(String email, UserConstants.ELoginType loginType);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(Long userId);
    Optional<User> findByLoginId(String loginId);
    boolean existsByEmailAndLoginType(String email, UserConstants.ELoginType loginType);
    boolean existsByNickname(String nickname);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.nickname = :nickname AND u != :user")
    boolean existsByNicknameNotUser(@Param("nickname") String nickname, @Param("user") User user);

    boolean existsByLoginId(String loginId);

    Optional<User> findByUsernameAndEmail(String username, String email);
    Optional<User> findByUsernameAndEmailAndLoginId(String username, String email, String loginId);

    @Query("select u from User u where u.userId = :userId")
    List<User> findByFirstUserId(@Param("userId") Long UserId);
}
