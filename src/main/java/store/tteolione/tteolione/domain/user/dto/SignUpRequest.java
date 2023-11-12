package store.tteolione.tteolione.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.tteolione.tteolione.domain.user.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "회원님의 로그인Id를 적어주세요.")
    @Pattern(
            regexp = "^(?=.*[a-z])[a-z0-9]{6,20}$",
            message = "id는 소문자 하나이상있어야하고, 6자~20자여야합니다."
    )
    private String loginId;

    @NotBlank(message = "회원님의 이메일를 적어주세요.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "회원님의 이름을 적어주세요.")
    private String username;

    @NotBlank(message = "회원님의 닉네임을 적어주세요.")
    private String nickname;

    @NotBlank(message = "회원의 비밀번호를 적어주세요.")
    @Pattern(
            regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "8~16 characters consisting of letters(A-Z, a-z), numbers, or special characters."
    )
    private String password;

    public User toAppEntity() {
        return User.builder()
                .loginId(loginId)
                .email(email)
                .username(username)
                .nickname(nickname)
                .password(password)
                .build();
    }
}
