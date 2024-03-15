package store.tteolione.tteolione.domain.user.dto.apple;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AppleSocialTokenInfoResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("refresh_token")
    @JsonInclude(JsonInclude.Include.NON_NULL) // refreshToken이 없을 때 null 반환
    private String refreshToken;

    @JsonProperty("id_token")
    private String idToken;
}
