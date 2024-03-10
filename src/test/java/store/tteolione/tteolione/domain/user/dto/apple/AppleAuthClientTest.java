package store.tteolione.tteolione.domain.user.dto.apple;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class AppleAuthClientTest {

    @Autowired
    GetAppleUserInfoService getUserInfoService;

    @Test
    void getToken() {
        String authorizationCode = "c7bff8c0443b548b6a1395e8ab9fed09a.0.ryzz.SnMnOi0DOA6dUm6sfdSEEw";

        String accessToken = getUserInfoService.getAccessToken(authorizationCode);
        getUserInfoService.revoke(accessToken);
//        var source = getUserInfoService.get(authorizationCode);
//        System.out.println("source = " + source.getEmail());
//        System.out.println("source = " + source.getSub());
//        System.out.println("source = " + source.isPrivateEmail());
    }
}