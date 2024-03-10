package store.tteolione.tteolione.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import store.tteolione.tteolione.domain.user.constant.UserConstants;
import store.tteolione.tteolione.domain.user.dto.*;
import store.tteolione.tteolione.domain.user.dto.apple.AppleIdTokenPayload;
import store.tteolione.tteolione.domain.user.dto.apple.AppleRevokeRequest;
import store.tteolione.tteolione.domain.user.dto.apple.GetAppleUserInfoService;
import store.tteolione.tteolione.domain.user.dto.apple.OAuth2AppleRequest;
import store.tteolione.tteolione.domain.user.dto.kakao.KakaoAuthClient;
import store.tteolione.tteolione.domain.user.dto.kakao.KakaoRevokeRequest;
import store.tteolione.tteolione.domain.user.dto.kakao.KakaoUserInfoResponse;
import store.tteolione.tteolione.domain.user.dto.kakao.OAuth2KakaoRequest;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.repository.UserRepository;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.dto.OAuth2Attribute;
import store.tteolione.tteolione.global.exception.GeneralException;
import store.tteolione.tteolione.global.jwt.TokenProvider;
import store.tteolione.tteolione.global.service.S3Service;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class OAuth2LoginServiceImpl implements OAuth2LoginService {

    private final GetAppleUserInfoService getAppleUserInfoService;
    private final KakaoAuthClient kakaoAuthClient;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final RedisTemplate redisTemplate;
    private final TokenProvider tokenProvider;

    @Value("${kakao.admin}")
    private String kakaoAdminKey;

    @Override
    public LoginResponse validateKakaoAccessToken(OAuth2KakaoRequest oAuth2KakaoRequest) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserConstants.EAuthority.eRoleUser.getValue()));
        KakaoUserInfoResponse kakaoResponse = kakaoAuthClient.getUserInfo("Bearer " + oAuth2KakaoRequest.getAccessToken());

        HashMap<String, Object> userInfo = getKakaoUserInfoHashMap(kakaoResponse);
        User saveUser;
        Optional<User> _user = userRepository.findByEmailAndLoginType(userInfo.get("email").toString(), UserConstants.ELoginType.eKakao);

        //회원가입
        if (!_user.isPresent()) {
            return LoginResponse.fromKakao(null, null, false, null);
        } else {
            //회원가입된 유저
            saveUser = _user.get();

            //탈퇴 유저 확인(2주동안 유지)
            List<GrantedAuthority> grantedAuthorities = saveUser.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                    .collect(Collectors.toList());
            for (GrantedAuthority grantedAuthority : grantedAuthorities) {
                if (grantedAuthority.getAuthority().equals("ROLE_WITHDRAW_USER")) {
                    throw new GeneralException(Code.WITH_DRAW_USER);
                }
            }

            saveUser.setTargetToken(oAuth2KakaoRequest.getTargetToken());
            saveUser.setProviderId(userInfo.get("kakaoUserId").toString());
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

        return LoginResponse.fromKakao(tokenInfoResponse, userInfo, true, saveUser);
    }

    @Override
    public LoginResponse signUpKakao(MultipartFile profile, OAuth2KakaoRequest oAuth2KakaoRequest) throws IOException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserConstants.EAuthority.eRoleUser.getValue()));
        KakaoUserInfoResponse kakaoResponse = kakaoAuthClient.getUserInfo("Bearer " + oAuth2KakaoRequest.getAccessToken());

        HashMap<String, Object> userInfo = getKakaoUserInfoHashMap(kakaoResponse);

        //이미 회원가입한 회원인지
        Optional<User> _user = userRepository.findByEmailAndLoginType(userInfo.get("email").toString(), UserConstants.ELoginType.eKakao);
        if (_user.isPresent()) {
            throw new GeneralException(Code.EXISTS_USER);
        }

        //이미지 업로드
        String userProfile = s3Service.uploadFile(profile);

        String randomNickname = createNickname();


        User saveUser = User.toKakaoUser(userInfo, userProfile, oAuth2KakaoRequest.getTargetToken(), randomNickname);
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

        return LoginResponse.fromKakao(tokenInfoResponse, userInfo, true, saveUser);
    }


    @Override
    public void kakaoRevoke(String providerId) {
        try {
            KakaoRevokeRequest kakaoRevokeRequest = KakaoRevokeRequest.builder()
                    .target_id_type("user_id")
                    .target_id(providerId)
                    .build();
            kakaoAuthClient.revoke("KakaoAK " + kakaoAdminKey, kakaoRevokeRequest);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Apple Revoke Error");
        }
    }

    private HashMap<String, Object> getKakaoUserInfoHashMap(KakaoUserInfoResponse kakaoResponse) {
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("kakaoUserId", kakaoResponse.id());
        userInfo.put("nickname", kakaoResponse.properties().nickname());
        userInfo.put("email", kakaoResponse.kakaoAccount().email());
        return userInfo;
    }

    @Override
    public LoginResponse validateAppleIdToken(OAuth2AppleRequest oAuth2AppleRequest) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserConstants.EAuthority.eRoleUser.getValue()));
        AppleIdTokenPayload appleIdTokenPayload = getAppleUserInfoService.get(oAuth2AppleRequest.getAuthorization());
        HashMap<String, Object> userInfo = getAppleUserInfoHashMap(appleIdTokenPayload);
        User saveUser;
        Optional<User> _user = userRepository.findByLoginTypeAndProviderId(UserConstants.ELoginType.eApple, appleIdTokenPayload.getSub());

        //회원가입
        if (!_user.isPresent()) {
            return LoginResponse.fromApple(null, null, false, null);
        } else {
            //회원가입된 유저
            saveUser = _user.get();

            //탈퇴 유저 확인(2주동안 유지)
            List<GrantedAuthority> grantedAuthorities = saveUser.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                    .collect(Collectors.toList());
            for (GrantedAuthority grantedAuthority : grantedAuthorities) {
                if (grantedAuthority.getAuthority().equals("ROLE_WITHDRAW_USER")) {
                    throw new GeneralException(Code.WITH_DRAW_USER);
                }
            }

            saveUser.setTargetToken(oAuth2AppleRequest.getTargetToken());
            saveUser.setProviderId(appleIdTokenPayload.getSub());
        }
        userInfo.put("userId", saveUser.getUserId());

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of("apple", "apple", userInfo);
        Map<String, Object> attributes = oAuth2Attribute.convertToMap();
        OAuth2User userDetails = new DefaultOAuth2User(authorities, attributes, "key");
        OAuth2AuthenticationToken auth = new OAuth2AuthenticationToken(userDetails, authorities, "key");
        auth.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);
        TokenInfoResponse tokenInfoResponse = tokenProvider.createToken(auth);
        redisTemplate.opsForValue()
                .set("RT:" + (String) auth.getPrincipal().getAttributes().get("email"),
                        tokenInfoResponse.getRefreshToken(), tokenInfoResponse.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return LoginResponse.fromApple(tokenInfoResponse, userInfo, true, saveUser);
    }

    @Override
    public LoginResponse signUpApple(MultipartFile profile, OAuth2AppleRequest oAuth2AppleRequest) throws IOException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserConstants.EAuthority.eRoleUser.getValue()));
        AppleIdTokenPayload appleIdTokenPayload = getAppleUserInfoService.get(oAuth2AppleRequest.getAuthorization());

        HashMap<String, Object> userInfo = getAppleUserInfoHashMap(appleIdTokenPayload);

        //이미 회원가입한 회원인지
        Optional<User> _user = userRepository.findByLoginTypeAndProviderId(UserConstants.ELoginType.eApple, appleIdTokenPayload.getSub());
        if (_user.isPresent()) {
            throw new GeneralException(Code.EXISTS_USER);
        }

        //이미지 업로드
        String userProfile = s3Service.uploadFile(profile);

        String randomNickname = createNickname();


        User saveUser = User.toAppleUser(userInfo, userProfile, oAuth2AppleRequest.getTargetToken(), randomNickname);
        userRepository.save(saveUser);

        userInfo.put("userId", saveUser.getUserId());

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of("apple", "apple", userInfo);
        Map<String, Object> attributes = oAuth2Attribute.convertToMap();
        OAuth2User userDetails = new DefaultOAuth2User(authorities, attributes, "key");
        OAuth2AuthenticationToken auth = new OAuth2AuthenticationToken(userDetails, authorities, "key");
        auth.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);
        TokenInfoResponse tokenInfoResponse = tokenProvider.createToken(auth);
        redisTemplate.opsForValue()
                .set("RT:" + (String) auth.getPrincipal().getAttributes().get("email"),
                        tokenInfoResponse.getRefreshToken(), tokenInfoResponse.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return LoginResponse.fromApple(tokenInfoResponse, userInfo, true, saveUser);
    }

    @Override
    public void appleRevoke(String authorizationCode) {
        String accessToken = getAppleUserInfoService.getAccessToken(authorizationCode);
        getAppleUserInfoService.revoke(accessToken);
    }

    private HashMap<String, Object> getAppleUserInfoHashMap(AppleIdTokenPayload appleIdTokenPayload) {
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", appleIdTokenPayload.getEmail());
        userInfo.put("sub", appleIdTokenPayload.getSub());
        return userInfo;
    }

    private String createNickname() {
        //랜덤 닉네임 생성
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        while (userRepository.existsByNickname("user_" + randomNumber)) {
            randomNumber = random.nextInt(9000) + 1000;
        }
        return "user_" + randomNumber;
    }
}
