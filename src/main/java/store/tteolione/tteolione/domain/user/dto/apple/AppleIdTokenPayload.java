package store.tteolione.tteolione.domain.user.dto.apple;

import lombok.Getter;

@Getter
public class AppleIdTokenPayload {

    private String sub;

    private String email;

    private boolean isPrivateEmail;
}
