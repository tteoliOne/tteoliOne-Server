package store.tteolione.tteolione.domain.user.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record KakaoUserInfoResponse(
        String id,
        Properties properties,

        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {

    @Builder
    public KakaoUserInfoResponse {
    }
}