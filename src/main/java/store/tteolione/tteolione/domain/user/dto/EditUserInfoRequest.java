package store.tteolione.tteolione.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUserInfoRequest {

    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 2, max = 6, message = "닉네임은 2글자 이상 6글자 이하이여야 합니다.")
    private String nickname;

    @NotBlank(message = "소개를 입력해주세요")
    private String intro;

}