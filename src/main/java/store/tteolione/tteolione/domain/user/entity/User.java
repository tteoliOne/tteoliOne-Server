package store.tteolione.tteolione.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import store.tteolione.tteolione.domain.likes.entity.Likes;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.product.entity.ProductTrade;
import store.tteolione.tteolione.domain.report.entity.Report;
import store.tteolione.tteolione.domain.review.entity.Review;
import store.tteolione.tteolione.domain.room.entity.Chat;
import store.tteolione.tteolione.domain.user.dto.SignUpRequest;
import store.tteolione.tteolione.global.entity.BaseTimeEntity;
import store.tteolione.tteolione.infra.email.entity.EmailAuth;


import java.util.*;

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
    private double ddabongScore;

    @Enumerated(EnumType.STRING)
    private ELoginType loginType;

    private String email;
    private boolean emailAuthChecked;
    private String provider;
    private String providerId;
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "email_auth_id")
    private EmailAuth emailAuth;

    @OneToOne(mappedBy = "seller", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private ProductTrade productTradeSeller;

    @OneToOne(mappedBy = "buyer", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private ProductTrade productTradeBuyer;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Product> productList = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "reporter", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Report> reporter = new ArrayList<>();

    @OneToMany(mappedBy = "joinMember", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Chat> chatList = new ArrayList<>();


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
                .providerId(null)
                .loginType(ELoginType.eApp)
                .emailAuth(emailAuth)
                .build();
    }


    public static User toKakaoUser(HashMap<String, Object> userInfo, String userProfile, String targetToken) {
        return User.builder()
                .loginId(userInfo.get("email").toString())
                .username(userInfo.get("nickname").toString())
                .nickname(userInfo.get("nickname").toString())
                .profile(userProfile)
                .email(userInfo.get("email").toString())
                .targetToken(targetToken)
                .loginType(ELoginType.eKakao)
                .providerId(userInfo.get("kakaoUserId").toString())
                .emailAuthChecked(true)
                .activated(true)
                .authorities(Collections.singleton(toRoleUserAuthority()))
                .build();

    }

    public static Authority toRoleWithDrawUserAuthority() {
        return Authority.builder()
                .authorityName(EAuthority.eWithdrawalUser.getValue())
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

    public void changeIntro(String newIntro) {
        this.intro = newIntro;
    }
}
