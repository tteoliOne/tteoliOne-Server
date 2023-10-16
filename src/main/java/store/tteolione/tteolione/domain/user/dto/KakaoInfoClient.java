package store.tteolione.tteolione.domain.user.dto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

//유저 정보 가져오기
@FeignClient(name = "kakaoInfoClient", url = "https://kapi.kakao.com")
public interface KakaoInfoClient {

    @GetMapping(value = "/v2/user/me", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    KakaoUserInfoResponse getUserInfo(
            @RequestHeader(name = "Authorization")
            String Authorization
    );

}