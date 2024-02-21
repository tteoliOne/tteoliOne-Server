package store.tteolione.tteolione.domain.user.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReissueRequest {

    @NotEmpty(message = "accessToken 을 입력해주세요.")
    private String accessToken;

    @NotEmpty(message = "refreshToken 을 입력해주세요.")
    private String refreshToken;

    private String targetToken;
}
