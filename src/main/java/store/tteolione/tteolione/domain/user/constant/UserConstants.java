package store.tteolione.tteolione.domain.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserConstants {

    @Getter
    @AllArgsConstructor
    public enum ELoginType {
        eGoogle("구글"),
        eNaver("네이버"),
        eKakao("카카오"),
        eApp("앱");
        private final String name;
    }

    @Getter
    @AllArgsConstructor
    public enum EKakaoUserInfo{
        eKakaoKeyAttribute("key"),
        eKakaoProfile("profile"),
        eKakaoNickNameAttribute("nickname"),
        eKakaoEmailAttribute("email"),
        eKakaoProfileImageAttribute("profile_image_url"),
        eKakaoGetMethod("GET"),
        eKakaoAuthorization("Authorization"),
        eKakaoBearer("Bearer "),
        eKakaoContentType("Content-Type"),
        eKakaoContentTypeUrlencoded("application/x-www-form-urlencoded"),
        eKakaoResponseCode("responseCode : "),
        eKakaoEmpty(""),
        eKakaoPropertiesAttribute("properties"),
        eKakaoAccountAttribute("kakao_account"),
        eKakao("kakao");
        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum EAuthority{
        eRoleDisabledUser("ROLE_DISABLED_USER"),
        eRoleUser("ROLE_USER");
        private final String value;
    }

}
