package store.tteolione.tteolione.domain.user.dto;

import lombok.*;

import java.util.HashMap;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String nickname;

    public static LoginResponse from(TokenInfoResponse tokenInfoResponse, HashMap<String, Object> userInfo) {
        return LoginResponse.builder()
                .accessToken(tokenInfoResponse.getAccessToken())
                .refreshToken(tokenInfoResponse.getRefreshToken())
                .nickname(userInfo.get("nickname").toString())
                .build();
    }
}
