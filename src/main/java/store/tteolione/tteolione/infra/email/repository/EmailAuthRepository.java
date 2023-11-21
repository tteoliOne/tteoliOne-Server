package store.tteolione.tteolione.infra.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.tteolione.tteolione.infra.email.entity.EmailAuth;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    Optional<EmailAuth> findByAuthCode(String authCode);
    Optional<EmailAuth> findTopByEmailOrderByUpdateAtDesc(String email);
}
