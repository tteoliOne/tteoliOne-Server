package store.tteolione.tteolione.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReissueResponse {

    private String accessToken;
    private String refreshToken;
}
