package store.tteolione.tteolione.domain.user.dto;

import lombok.Builder;

public record KakaoAccount(
        String email,
        Profile profile
) {

    @Builder
    public KakaoAccount {
    }
}

