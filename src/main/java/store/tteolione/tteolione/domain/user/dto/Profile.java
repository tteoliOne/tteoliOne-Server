package store.tteolione.tteolione.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record Profile(
        String nickname,
        @JsonProperty("profile_image_url")
        String profileImageUrl
) {
    @Builder
    public Profile{

    }
}
