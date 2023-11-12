package store.tteolione.tteolione.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import store.tteolione.tteolione.domain.user.constant.UserConstants;
import store.tteolione.tteolione.domain.user.dto.*;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.repository.UserRepository;
import store.tteolione.tteolione.global.dto.OAuth2Attribute;
import store.tteolione.tteolione.global.jwt.TokenProvider;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class OAuth2LoginServiceImpl implements OAuth2LoginService {

    private final KakaoInfoClient kakaoInfoClient;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;
    private final TokenProvider tokenProvider;

    @Override
    @Transactional
    public LoginResponse validateKakaoAccessToken(OAuth2KakaoRequest oAuth2KakaoRequest) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(UserConstants.EAuthority.eRoleUser.getValue()));
        KakaoUserInfoResponse kakaoResponse = kakaoInfoClient.getUserInfo("Bearer " + oAuth2KakaoRequest.getAccessToken());

        HashMap<String, Object> userInfo = getUserInfoHashMap(kakaoResponse);
        User saveUser = saveKakao(userInfo);
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

        return LoginResponse.fromKakao(tokenInfoResponse, userInfo);
    }

    private HashMap<String, Object> getUserInfoHashMap(KakaoUserInfoResponse kakaoResponse) {
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("nickname", kakaoResponse.properties().nickname());
        userInfo.put("email", kakaoResponse.kakaoAccount().email());
        userInfo.put("profile", kakaoResponse.kakaoAccount().profile().profileImageUrl());
        return userInfo;
    }

    private User saveKakao(HashMap<String, Object> kakaoUserInfo) {
        User user = userRepository.findByEmailAndLoginType(kakaoUserInfo.get("email").toString(), UserConstants.ELoginType.eKakao)
                .orElse(User.toKakaoUser(kakaoUserInfo));
        return userRepository.save(user);
    }


    /**
     * validate
     */


}
