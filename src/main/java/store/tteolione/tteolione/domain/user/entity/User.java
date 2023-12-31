package store.tteolione.tteolione.domain.user.entity;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import store.tteolione.tteolione.domain.user.dto.SignUpRequest;
import store.tteolione.tteolione.global.entity.BaseTimeEntity;
import store.tteolione.tteolione.infra.email.entity.EmailAuth;

import javax.persistence.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import static store.tteolione.tteolione.domain.user.constant.UserConstants.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String loginId;
    private String password;

    private String username;
    private String nickname;
    private String intro;
    private String profile;
    private String targetToken;

    @Enumerated(EnumType.STRING)
    private ELoginType loginType;

    private String email;
    private boolean emailAuthChecked;
    private String provider;
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    public static User toAppEntity(SignUpRequest signUpRequest, PasswordEncoder passwordEncoder, EmailAuth emailAuth, String profile) {
        return User.builder()
                .loginId(signUpRequest.getLoginId())
                .email(signUpRequest.getEmail())
                .username(signUpRequest.getUsername())
                .nickname(signUpRequest.getNickname())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .authorities(Collections.singleton(toRoleUserAuthority()))
                .profile(profile)
                .activated(true)
                .emailAuthChecked(true)
                .loginType(ELoginType.eApp)
                .emailAuth(emailAuth)
                .build();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_auth_id")
    private EmailAuth emailAuth;

    public static User toKakaoUser(HashMap<String, Object> userInfo, String userProfile) {
        return User.builder()
                .loginId(userInfo.get("email").toString())
                .username(userInfo.get("nickname").toString())
                .nickname(userInfo.get("nickname").toString())
                .profile(userProfile)
                .email(userInfo.get("email").toString())
                .loginType(ELoginType.eKakao)
                .emailAuthChecked(true)
                .activated(true)
                .authorities(Collections.singleton(toRoleUserAuthority()))
                .build();

    }

    public static Authority toRoleDisabledUserAuthority() {
        return Authority.builder()
                .authorityName(EAuthority.eRoleDisabledUser.getValue())
                .build();
    }

    public static Authority toRoleUserAuthority() {
        return Authority.builder()
                .authorityName(EAuthority.eRoleUser.getValue())
                .build();
    }

    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }
}
