package store.tteolione.tteolione.domain.user.dto.kakao;

import lombok.Builder;

public record Properties(
        String nickname
) {
    @Builder
    public Properties {}
}
