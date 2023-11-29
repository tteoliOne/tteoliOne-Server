package store.tteolione.tteolione.infra.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.tteolione.tteolione.infra.email.entity.EmailAuth;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    Optional<EmailAuth> findByEmailAndAuthCode(String email, String authCode);
    Optional<EmailAuth> findByEmail(String email);
    Optional<EmailAuth> findTopByEmailOrderByUpdateAtDesc(String email);
}
