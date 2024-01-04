package store.tteolione.tteolione.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.user.constant.UserConstants;
import store.tteolione.tteolione.domain.user.dto.*;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.repository.UserRepository;
import store.tteolione.tteolione.global.dto.OAuth2Attribute;
import store.tteolione.tteolione.global.exception.GeneralException;
import store.tteolione.tteolione.global.jwt.TokenProvider;
import store.tteolione.tteolione.global.service.S3Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Transactional
public class OAuth2LoginServiceImpl implements OAuth2LoginService {

    private final KakaoInfoClient kakaoInfoClient;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final RedisTemplate redisTemplate;
    private final TokenProvider tokenProvider;

    @Override
    public LoginResponse validateKakaoAccessToken(OAuth2KakaoRequest oAuth2KakaoRequest) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserConstants.EAuthority.eRoleUser.getValue()));
        KakaoUserInfoResponse kakaoResponse = kakaoInfoClient.getUserInfo("Bearer " + oAuth2KakaoRequest.getAccessToken());

        HashMap<String, Object> userInfo = getUserInfoHashMap(kakaoResponse);
        User saveUser;
        Optional<User> _user = userRepository.findByEmailAndLoginType(userInfo.get("email").toString(), UserConstants.ELoginType.eKakao);
        if (!_user.isPresent()) {
            return LoginResponse.fromKakao(null, null, false);
        } else {
            saveUser = _user.get();
        }
        userInfo.put("userId", saveUser.getUserId());

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of("kakao", "kakao", userInfo);
        Map<String, Object> attributes = oAuth2Attribute.convertToMap();
        OAuth2User userDetails = new DefaultOAuth2User(authorities, attributes, "key");
        OAuth2AuthenticationToken auth = new OAuth2AuthenticationToken(userDetails, authorities, "key");
        auth.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);
        TokenInfoResponse tokenInfoResponse = tokenProvider.createToken(auth);
        redisTemplate.opsForValue()
                .set("RT:" + (String) auth.getPrincipal().getAttributes().get("email"),
                        tokenInfoResponse.getRefreshToken(), tokenInfoResponse.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        //TODO targetToken 로직

        return LoginResponse.fromKakao(tokenInfoResponse, userInfo, true);
    }

    @Override
    public LoginResponse signUpKakao(MultipartFile profile, OAuth2KakaoRequest oAuth2KakaoRequest) throws IOException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserConstants.EAuthority.eRoleUser.getValue()));
        System.out.println("oAuth2KakaoRequest = " + oAuth2KakaoRequest.getAccessToken());
        KakaoUserInfoResponse kakaoResponse = kakaoInfoClient.getUserInfo("Bearer " + oAuth2KakaoRequest.getAccessToken());

        HashMap<String, Object> userInfo = getUserInfoHashMap(kakaoResponse);

        //이미 회원가입한 회원인지
        Optional<User> _user = userRepository.findByEmailAndLoginType(userInfo.get("email").toString(), UserConstants.ELoginType.eKakao);
        if (_user.isPresent()) {
            throw new GeneralException("이미 회원가입한 회원입니다.");
        }

        //이미지 업로드
        String userProfile = s3Service.uploadFile(profile);

        User saveUser = User.toKakaoUser(userInfo, userProfile);
        userRepository.save(saveUser);

        userInfo.put("userId", saveUser.getUserId());

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of("kakao", "kakao", userInfo);
        Map<String, Object> attributes = oAuth2Attribute.convertToMap();
        OAuth2User userDetails = new DefaultOAuth2User(authorities, attributes, "key");
        OAuth2AuthenticationToken auth = new OAuth2AuthenticationToken(userDetails, authorities, "key");
        auth.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);
        TokenInfoResponse tokenInfoResponse = tokenProvider.createToken(auth);
        redisTemplate.opsForValue()
                .set("RT:" + (String) auth.getPrincipal().getAttributes().get("email"),
                        tokenInfoResponse.getRefreshToken(), tokenInfoResponse.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        //TODO targetToken 로직

        return LoginResponse.fromKakao(tokenInfoResponse, userInfo, true);
    }

    private HashMap<String, Object> getUserInfoHashMap(KakaoUserInfoResponse kakaoResponse) {
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("nickname", kakaoResponse.properties().nickname());
        userInfo.put("email", kakaoResponse.kakaoAccount().email());
        return userInfo;
    }

}
