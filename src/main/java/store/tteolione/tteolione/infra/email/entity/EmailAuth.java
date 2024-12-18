package store.tteolione.tteolione.infra.email.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.global.entity.BaseTimeEntity;

import java.util.Collections;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuth extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailAuthId;

    private String email;
    private String authCode;
    private boolean emailAuthChecked;

    @OneToOne(mappedBy = "emailAuth", fetch = FetchType.LAZY)
    private User user;

    public static EmailAuth createEmailAuth(String email, String authCode) {
        EmailAuth emailAuth = new EmailAuth();
        emailAuth.setEmail(email);
        emailAuth.setAuthCode(authCode);
        emailAuth.setEmailAuthChecked(false);
        emailAuth.setUser(null);
        return emailAuth;
    }

    public static EmailAuth createEmailAuth(String email) {
        EmailAuth emailAuth = new EmailAuth();
        emailAuth.setEmail(email);
        emailAuth.setEmailAuthChecked(true);
        emailAuth.setUser(null);
        return emailAuth;
    }

    public void verifyEmail() {
        this.setEmailAuthChecked(true);
    }

    public void completeAuth() {
        this.getUser().setEmailAuthChecked(true);
        this.getUser().setAuthorities(Collections.singleton(User.toRoleUserAuthority()));
    }

}
