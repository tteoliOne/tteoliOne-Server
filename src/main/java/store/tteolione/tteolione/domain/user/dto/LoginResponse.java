package store.tteolione.tteolione.domain.user.dto;

import lombok.*;
import store.tteolione.tteolione.domain.user.dto.apple.AppleIdTokenPayload;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.HashMap;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    //기존회원일때
    private boolean existsUser;
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private String nickname;

    // Apple 로그인 시에만 사용될 refresh token
    private String appleRefreshToken;


    public static LoginResponse fromKakao(TokenInfoResponse tokenInfoResponse, HashMap<String, Object> userInfo, boolean existsUser, User user) {
        return LoginResponse.builder()
                .existsUser(existsUser)
                .userId(userInfo == null ? null : Long.valueOf(userInfo.get("userId").toString()))
                .accessToken(tokenInfoResponse == null ? null : tokenInfoResponse.getAccessToken())
                .refreshToken(tokenInfoResponse == null ? null : tokenInfoResponse.getRefreshToken())
                .nickname(userInfo == null ? null : user.getNickname())
                .build();
    }

    public static LoginResponse fromApp(User user, TokenInfoResponse tokenInfoResponse) {
        return LoginResponse.builder()
                .existsUser(true)
                .userId(user.getUserId())
                .accessToken(tokenInfoResponse.getAccessToken())
                .refreshToken(tokenInfoResponse.getRefreshToken())
                .nickname(user.getNickname())
                .build();
    }

    public static LoginResponse fromApple(TokenInfoResponse tokenInfoResponse, HashMap<String, Object> userInfo, boolean existsUser, User user) {
        return LoginResponse.builder()
                .existsUser(existsUser)
                .userId(userInfo == null ? null : Long.valueOf(userInfo.get("userId").toString()))
                .accessToken(tokenInfoResponse == null ? null : tokenInfoResponse.getAccessToken())
                .refreshToken(tokenInfoResponse == null ? null : tokenInfoResponse.getRefreshToken())
                .nickname(userInfo == null ? null : user.getNickname())
                .build();
    }

    public static LoginResponse fromAppleNRU(TokenInfoResponse tokenInfoResponse, HashMap<String, Object> userInfo, boolean existsUser, User user, String appleRefreshToken) {
        return LoginResponse.builder()
                .existsUser(existsUser)
                .userId(userInfo == null ? null : Long.valueOf(userInfo.get("userId").toString()))
                .accessToken(tokenInfoResponse == null ? null : tokenInfoResponse.getAccessToken())
                .refreshToken(tokenInfoResponse == null ? null : tokenInfoResponse.getRefreshToken())
                .nickname(userInfo == null ? null : user.getNickname())
                .appleRefreshToken(appleRefreshToken) // Apple 로그인 시에만 refresh token 추가
                .build();
    }
}
