package store.tteolione.tteolione.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "회원의 로그인Id를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-z])[a-z0-9]{6,20}$",
            message = "id는 소문자 하나이상있어야하고, 6자~20자여야합니다."
    )
    private String loginId;

    @NotBlank(message = "회원의 비밀번호를 입력해 주세요.")
    @Pattern(
            regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "8~16 characters consisting of letters(A-Z, a-z), numbers, or special characters."
    )
    private String password;

//    private String targetToken;
}
