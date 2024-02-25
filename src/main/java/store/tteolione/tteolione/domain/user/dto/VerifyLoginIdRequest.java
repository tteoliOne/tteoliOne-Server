package store.tteolione.tteolione.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyLoginIdRequest {

    @NotBlank(message = "회원님의 이름을 적어주세요.")
    private String username;

    @Size(min = 7, max = 7, message = "인증코드는 7자리입니다.")
    private String authCode;

    @NotBlank(message = "회원님의 이메일을 적어주세요.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;
}
