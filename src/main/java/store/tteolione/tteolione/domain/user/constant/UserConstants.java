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
        eApple("애플"),
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
    public enum EBaseUserInfo {
        eBaseProfile("https://tteolione-bucket.s3.ap-northeast-2.amazonaws.com/test/00c04127-9497-43f5-afd5-86937dba5e97.png");
        private String value;
        EBaseUserInfo(String value) {this.value = value;}
    }


    @Getter
    @AllArgsConstructor
    public enum EAuthority{
        eRoleDisabledUser("ROLE_DISABLED_USER"),
        eRoleUser("ROLE_USER"),
        eWithdrawalUser("ROLE_WITHDRAW_USER");
        private final String value;
    }

}
