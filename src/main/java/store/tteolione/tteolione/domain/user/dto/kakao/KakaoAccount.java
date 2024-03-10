package store.tteolione.tteolione.domain.user.dto.kakao;

import lombok.Builder;

public record KakaoAccount(
        String email,
        Profile profile
) {

    @Builder
    public KakaoAccount {
    }
}

