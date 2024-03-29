package store.tteolione.tteolione.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DupNicknameRequest {

    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 2, max = 6, message = "닉네임은 2글자 이상 6글자 이하이여야 합니다.")
    private String nickname;
}
