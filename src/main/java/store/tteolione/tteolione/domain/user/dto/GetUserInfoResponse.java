package store.tteolione.tteolione.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.tteolione.tteolione.domain.user.entity.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserInfoResponse {

    private String profile;
    private String nickname;
    private String intro;
    private int thumbsUpScore;

    public static GetUserInfoResponse toData(User user) {
        return GetUserInfoResponse.builder()
                .profile(user.getProfile())
                .nickname(user.getNickname())
                .intro(user.getIntro())
                .thumbsUpScore(0)
                .build();
    }
}
