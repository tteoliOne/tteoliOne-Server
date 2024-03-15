package store.tteolione.tteolione.domain.user.dto.apple;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2AppleRequest {

    private String authorization;
    private String targetToken;
    private String appleRefreshToken;
}
