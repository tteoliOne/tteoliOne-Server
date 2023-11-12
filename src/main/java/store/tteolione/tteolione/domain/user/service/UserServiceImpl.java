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
import store.tteolione.tteolione.domain.user.constant.UserConstants;
import store.tteolione.tteolione.domain.user.dto.*;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.repository.UserRepository;
import store.tteolione.tteolione.global.exception.GeneralException;
import store.tteolione.tteolione.global.jwt.TokenProvider;
import store.tteolione.tteolione.infra.email.entity.EmailAuth;
import store.tteolione.tteolione.infra.email.repository.EmailAuthRepository;

import javax.persistence.EntityNotFoundException;
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
            throw new GeneralException("유효하지 않은 Refresh Token입니다.");
        }
        Authentication authentication = tokenProvider.getAuthentication(reissueRequest.getAccessToken());
        String refreshToken = getRefreshToken(authentication);

        if (ObjectUtils.isEmpty(refreshToken)) {
            throw new GeneralException("잘못된 Refresh Token입니다.");
        }
        if (!refreshToken.equals(reissueRequest.getRefreshToken())) {
            throw new GeneralException("일치하는 Refresh Token이 없습니다.");
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
    public void signUpUser(SignUpRequest signUpRequest) {
        //이미 등록된 회원인지
        validateIsAlreadyRegisteredUser(signUpRequest.getEmail());
        //이미 이메일이 있는지
        validateIsAlreadyRegisteredEmail(signUpRequest.getEmail());
        //보낸 이메일이 인증되었는지 확인
        EmailAuth findEmailAuth = validateEmailAuthEntity(signUpRequest.getEmail());

        userRepository.save(User.toAppEntity(signUpRequest, passwordEncoder, findEmailAuth));
    }

    @Override
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
    }

    private EmailAuth validateEmailAuthEntity(String email) {
        EmailAuth emailAuth = emailAuthRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException("본인인증 버튼을 눌러주세요.")); //이메일 전송도 안되었을때
        if (!emailAuth.isEmailAuthChecked()) {
            throw new GeneralException("이메일 인증이 되어있지 않습니다.");
        }
        return emailAuth;
    }

    @Override
    public void validateIsAlreadyRegisteredUser(String email) {
        Optional<User> findUser = userRepository.findByEmailAndLoginType(email, UserConstants.ELoginType.eApp);
        if (findUser.isPresent()) {
            User user = findUser.get();
            if (user.isEmailAuthChecked()) {
                throw new GeneralException("이미 등록된 회원입니다.");
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
                .orElseThrow(() -> new GeneralException("아이디나 비밀번호를 다시 확인해주세요."));
        if (!passwordEncoder.matches(loginRequest.getPassword(), findUser.getPassword())) {
            throw new GeneralException("아이디나 비밀번호를 다시 확인해주세요.");
        }
        if (!findUser.isActivated()) {
            throw new GeneralException("비활성화 유저입니다.");
        }
        TokenInfoResponse tokenInfoResponse = validateLogin(loginId, loginRequest.getPassword());

        return LoginResponse.fromApp(findUser, tokenInfoResponse);
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
