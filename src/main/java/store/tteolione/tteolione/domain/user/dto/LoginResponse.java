package store.tteolione.tteolione.domain.user.dto;

import lombok.*;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.HashMap;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private Long userId;
    private String accessToken;
    private String refreshToken;
    private String nickname;

    public static LoginResponse fromKakao(TokenInfoResponse tokenInfoResponse, HashMap<String, Object> userInfo) {
        return LoginResponse.builder()
                .userId(Long.valueOf(userInfo.get("userId").toString()))
                .accessToken(tokenInfoResponse.getAccessToken())
                .refreshToken(tokenInfoResponse.getRefreshToken())
                .nickname(userInfo.get("nickname").toString())
                .build();
    }

    public static LoginResponse fromApp(User user, TokenInfoResponse tokenInfoResponse) {
        return LoginResponse.builder()
                .userId(user.getUserId())
                .accessToken(tokenInfoResponse.getAccessToken())
                .refreshToken(tokenInfoResponse.getRefreshToken())
                .nickname(user.getNickname())
                .build();
    }
}
