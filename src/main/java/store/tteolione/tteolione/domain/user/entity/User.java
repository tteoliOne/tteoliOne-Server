package store.tteolione.tteolione.domain.user.entity;

import lombok.*;
import store.tteolione.tteolione.global.entity.BaseTimeEntity;

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

    private String nickname;
    private String intro;
    private String profile;
    private String targetToken;

    @Enumerated(EnumType.STRING)
    private ELoginType loginType;

    private String email;
    private boolean emailAuthChecked;
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;


    public static User toKakaoUser(HashMap<String, Object> userInfo) {
        return User.builder()
                .loginId(EKakaoUserInfo.eKakao.getValue()+userInfo.get("email").toString())
                .nickname(userInfo.get("nickname").toString())
                .profile(userInfo.get("profile").toString())
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
}
