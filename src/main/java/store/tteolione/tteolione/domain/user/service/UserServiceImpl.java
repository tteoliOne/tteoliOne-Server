package store.tteolione.tteolione.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.user.constant.UserConstants;
import store.tteolione.tteolione.domain.user.dto.*;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.repository.UserRepository;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.exception.GeneralException;
import store.tteolione.tteolione.global.jwt.TokenProvider;
import store.tteolione.tteolione.global.service.S3Service;
import store.tteolione.tteolione.infra.email.dto.VerifyEmailRequest;
import store.tteolione.tteolione.infra.email.entity.EmailAuth;
import store.tteolione.tteolione.infra.email.repository.EmailAuthRepository;
import store.tteolione.tteolione.infra.email.service.EmailService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final EmailAuthRepository emailAuthRepository;
    private final EmailService emailService;
    private final S3Service s3Service;


    @Override
    public UserDetails loadUserByUsername(final String loginId) {
        return this.userRepository.findOneWithAuthoritiesByLoginId(loginId)
                .map(user -> createUser(loginId, user))
                .orElseThrow(() -> new UsernameNotFoundException(loginId + "유저 이름을 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(String username, User user) {
        if (!user.isActivated()) throw new GeneralException("유저가 활성화되어 있지 않습니다.");
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getLoginId(),
                user.getPassword(),
                grantedAuthorities);
    }

    @Override
    public ReissueResponse reissueToken(ReissueRequest reissueRequest) {
        if (tokenProvider.validateRefreshToken(reissueRequest.getRefreshToken())) {
            throw new GeneralException(Code.VALIDATION_REFRESH_TOKEN);
        }
        Authentication authentication = tokenProvider.getAuthentication(reissueRequest.getAccessToken());
        String refreshToken = getRefreshToken(authentication);

        if (ObjectUtils.isEmpty(refreshToken)) {
            throw new GeneralException(Code.WRONG_REFRESH_TOKEN);
        }
        if (!refreshToken.equals(reissueRequest.getRefreshToken())) {
            throw new GeneralException(Code.NOT_EXISTS_REFRESH_TOKEN);
        }
        TokenInfoResponse tokenInfoResponse = tokenProvider.createToken(authentication);
        this.redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(),
                        tokenInfoResponse.getRefreshToken(), tokenInfoResponse.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        return new ReissueResponse(tokenInfoResponse.getAccessToken(), tokenInfoResponse.getRefreshToken());
    }

    private String getRefreshToken(Authentication authentication) {
        if (authentication.getName().equals("email")) {
//            return (String) redisTemplate.opsForValue().get("RT:" + (String) authentication.getAuthorities().getAttributes().get("email"));
        }
        return (String) redisTemplate.opsForValue().get("RT:" + authentication.getName());
    }

    @Override
    public void signUpUser(SignUpRequest signUpRequest, MultipartFile profile) throws IOException {
        //이미 등록된 회원인지
        validateIsAlreadyRegisteredUser(signUpRequest.getEmail());
        //보낸 이메일이 인증되었는지 확인
        EmailAuth findEmailAuth = validateEmailAuthEntity(signUpRequest.getEmail());
        String saveProfile = s3Service.uploadFile(profile);

        userRepository.save(User.toAppEntity(signUpRequest, passwordEncoder, findEmailAuth, saveProfile));
    }

    @Override
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
    }

    private EmailAuth validateEmailAuthEntity(String email) {
        EmailAuth emailAuth = emailAuthRepository.findTopByEmailOrderByUpdateAtDesc(email)
                .orElseThrow(() -> new GeneralException(Code.VALIDATION_EMAIL)); //이메일 전송도 안되었을때
        if (!emailAuth.isEmailAuthChecked()) {
            throw new GeneralException(Code.VALIDATION_EMAIL);
        }
        return emailAuth;
    }

    @Override
    public void validateIsAlreadyRegisteredUser(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            User user = findUser.get();
            if (user.isEmailAuthChecked()) {
                switch (user.getLoginType()) {
                    case eApp -> throw new GeneralException(Code.EXISTS_USER);
                    case eKakao -> throw new GeneralException(Code.EXISTS_KAKAO);
                    case eGoogle -> throw new GeneralException(Code.EXISTS_GOOGLE);
                    case eNaver -> throw new GeneralException(Code.EXISTS_NAVER);
                    case eApple -> throw new GeneralException(Code.EXISTS_APPLE);
                }
            }
        }
    }

    @Override
    public void validateIsAlreadyRegisteredEmail(String email) {
        Optional<User> findUser = userRepository.findByEmailAndLoginType(email, UserConstants.ELoginType.eApp);
        if (findUser.isPresent()) {
            User user = findUser.get();
            if (user.isEmailAuthChecked()) {
                throw new GeneralException("이미 등록된 이메일입니다.");
            }
        }
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        String loginId = loginRequest.getLoginId();
        User findUser = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_LOGIN_ID_PW));
        if (!passwordEncoder.matches(loginRequest.getPassword(), findUser.getPassword())) {
            throw new GeneralException(Code.NOT_EXISTS_LOGIN_ID_PW);
        }
        if (!findUser.isActivated()) {
            throw new GeneralException(Code.DISABLED_USER);
        }
        findUser.setTargetToken(loginRequest.getTargetToken());
        TokenInfoResponse tokenInfoResponse = validateLogin(loginId, loginRequest.getPassword());

        return LoginResponse.fromApp(findUser, tokenInfoResponse);
    }

    @Override
    public void duplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new GeneralException(Code.EXIST_NICKNAME);
        }
    }

    @Override
    public void duplicateLoginId(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new GeneralException(Code.EXISTS_LOGIN_ID);
        }
    }

    @Override
    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_USER));
    }

    @Override
    public String findLoginId(FindIdRequest findIdRequest) throws Exception {
        User findUser = userRepository.findByUsernameAndEmail(findIdRequest.getUsername(), findIdRequest.getEmail())
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER_INFO));
        switch (findUser.getLoginType()) {
            case eKakao -> throw new GeneralException(Code.FOUND_KAKAO_USER);
            case eGoogle -> throw new GeneralException(Code.FOUND_GOOGLE_USER);
            case eNaver -> throw new GeneralException(Code.FOUND_NAVER_USER);
            case eApple -> throw new GeneralException(Code.FOUND_APPLE_USER);
        }
        emailService.sendEmailAuth(findIdRequest.getEmail());
        return "이메일 전송 성공";
    }

    @Override
    public VerifyLoginIdResponse verifyLoginId(VerifyLoginIdRequest verifyLoginIdRequest) {
        boolean isVerify = emailService.verifyEmailCode(new VerifyEmailRequest(verifyLoginIdRequest.getAuthCode(), verifyLoginIdRequest.getEmail()));
        if (!isVerify) {
            throw new GeneralException(Code.VERIFY_EMAIL_CODE);
        }
        User findUser = userRepository.findByUsernameAndEmail(verifyLoginIdRequest.getUsername(), verifyLoginIdRequest.getEmail())
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER_INFO));
        return new VerifyLoginIdResponse(findUser.getLoginId());
    }

    @Override
    public String findPassword(FindPasswordRequest findPasswordRequest) throws Exception {
        User findUser = userRepository.findByUsernameAndEmailAndLoginId(findPasswordRequest.getUsername(), findPasswordRequest.getEmail(), findPasswordRequest.getLoginId())
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER_INFO));
        switch (findUser.getLoginType()) {
            case eKakao -> throw new GeneralException(Code.FOUND_KAKAO_USER);
            case eGoogle -> throw new GeneralException(Code.FOUND_GOOGLE_USER);
            case eNaver -> throw new GeneralException(Code.FOUND_NAVER_USER);
            case eApple -> throw new GeneralException(Code.FOUND_APPLE_USER);
        }
        emailService.sendEmailAuth(findPasswordRequest.getEmail());
        return "이메일 전송 성공";
    }

    @Override
    public String verifyPassword(VerifyPasswordRequest verifyPasswordRequest) {
        boolean isVerify = emailService.verifyEmailCode(new VerifyEmailRequest(verifyPasswordRequest.getAuthCode(), verifyPasswordRequest.getEmail()));
        if (!isVerify) {
            throw new GeneralException(Code.VERIFY_EMAIL_CODE);
        }
        userRepository.findByUsernameAndEmailAndLoginId(verifyPasswordRequest.getUsername(), verifyPasswordRequest.getEmail(), verifyPasswordRequest.getLoginId())
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER_INFO));
        return "비밀번호 이메일 검증 성공";
    }

    @Override
    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User findUser = userRepository.findByUsernameAndEmailAndLoginId(resetPasswordRequest.getUsername(), resetPasswordRequest.getEmail(), resetPasswordRequest.getLoginId())
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER_INFO));
        switch (findUser.getLoginType()) {
            case eKakao -> throw new GeneralException(Code.FOUND_KAKAO_USER);
            case eGoogle -> throw new GeneralException(Code.FOUND_GOOGLE_USER);
            case eNaver -> throw new GeneralException(Code.FOUND_NAVER_USER);
            case eApple -> throw new GeneralException(Code.FOUND_APPLE_USER);
        }
        if (passwordEncoder.matches(resetPasswordRequest.getPassword(), findUser.getPassword())) {
            throw new GeneralException(Code.MATCH_EXIST_PW);
        }
        findUser.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));

        return "비밀번호 재설정 성공";
    }

    @Override
    public void changeNickname(ChangeNicknameRequest changeNicknameRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = findByLoginId(authentication.getName());

        String newNickname = changeNicknameRequest.getNickname();
        if (user.getNickname().equals(newNickname)) {
            throw new GeneralException(Code.EQUALS_NICKNAME);
        }
        duplicateNickname(newNickname);

        user.changeNickname(newNickname);
    }

    @Override
    public void editUserInfo(EditUserInfoRequest editUserInfoRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = findByLoginId(authentication.getName());

        String newNickname = editUserInfoRequest.getNickname();
        String newIntro = editUserInfoRequest.getIntro();

        //닉네임 자기꺼 빼고 존재하는지
        if (userRepository.existsByNicknameNotUser(newNickname, user)) {
            throw new GeneralException(Code.EXIST_NICKNAME);
        }

        user.changeNickname(newNickname);
        user.changeIntro(newIntro);

    }

    @Override
    public GetUserInfoResponse getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = findByLoginId(authentication.getName());
        return GetUserInfoResponse.toData(user);
    }


    private TokenInfoResponse validateLogin(String loginId, String password) {
        UserDetails userDetails = loadUserByUsername(loginId);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        TokenInfoResponse tokenInfoResponse = this.tokenProvider.createToken(authenticationToken);
        this.redisTemplate.opsForValue()
                .set("RT:" + authenticationToken.getName(),
                        tokenInfoResponse.getRefreshToken(), tokenInfoResponse.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        return tokenInfoResponse;
    }
}
