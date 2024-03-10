package store.tteolione.tteolione.domain.user.dto.apple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AppleRevokeRequest {

    private String client_id;
    private String client_secret;
    private String token;
    private String token_type_hint;
}
