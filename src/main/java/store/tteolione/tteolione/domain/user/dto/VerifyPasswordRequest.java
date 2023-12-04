package store.tteolione.tteolione.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyPasswordRequest {
    @NotBlank(message = "회원님의 이름을 적어주세요.")
    private String username;

    @Size(min = 7, max = 7, message = "인증코드는 7자리입니다.")
    private String authCode;

    @NotBlank(message = "회원님의 이메일을 적어주세요.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "회원님의 로그인Id를 적어주세요.")
    @Pattern(
            regexp = "^(?=.*[a-z])[a-z0-9]{6,20}$",
            message = "id는 소문자 하나이상있어야하고, 6자~20자여야합니다."
    )
    private String loginId;
}
