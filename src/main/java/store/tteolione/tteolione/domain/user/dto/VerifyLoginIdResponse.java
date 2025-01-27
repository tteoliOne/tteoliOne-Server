package store.tteolione.tteolione.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VerifyLoginIdResponse {

    private String loginId;

    @Builder
    public VerifyLoginIdResponse(String loginId) {
        this.loginId = loginId;
    }

    public static VerifyLoginIdResponse from(String loginId) {
        return VerifyLoginIdResponse.builder()
                .loginId(loginId)
                .build();
    }
}
