package store.tteolione.tteolione.domain.user.dto;

import lombok.Builder;

public record Properties(
        String nickname
) {
    @Builder
    public Properties {}
}
