package store.tteolione.tteolione.domain.user.dto.apple;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2AppleRequest {

    @NotBlank(message = "애플의 인가코드를 입력해 주세요.")
    private String authorization;

    private String targetToken;
}
