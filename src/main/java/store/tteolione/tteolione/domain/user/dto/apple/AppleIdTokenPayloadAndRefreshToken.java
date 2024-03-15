package store.tteolione.tteolione.domain.user.dto.apple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AppleIdTokenPayloadAndRefreshToken {

    private String refreshToken;
    private AppleIdTokenPayload appleIdTokenPayload;

}
