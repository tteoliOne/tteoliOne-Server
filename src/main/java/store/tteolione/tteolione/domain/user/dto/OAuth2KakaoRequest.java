package store.tteolione.tteolione.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2KakaoRequest {

    @NotBlank(message = "카카오의 accessToken을 입력해 주세요.")
    private String accessToken;

    private String targetToken;
}
