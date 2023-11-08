package store.tteolione.tteolione.infra.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyEmailRequest {
    @Size(min = 7, max = 7, message = "인증코드는 7자리입니다.")
    private String authCode;
}
