package store.tteolione.tteolione.global.dto;

import lombok.Builder;
import lombok.Getter;
import store.tteolione.tteolione.domain.user.dto.KakaoUserInfoResponse;
import store.tteolione.tteolione.domain.user.entity.Authority;
import store.tteolione.tteolione.domain.user.entity.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Getter
@Builder
public class OAuth2Attribute {

    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String name;
    private String picture;

    public static OAuth2Attribute of(String provider, String attributeKey,
                                     Map<String, Object> attributes) {
        switch (provider) {
            case "kakao":
                return ofKakao("email", attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attribute ofKakao(String attributeKey,
                                           Map<String, Object> attributes) {

        return OAuth2Attribute.builder()
                .name((String) attributes.get("nickname"))
                .email((String) attributes.get("email"))
                .picture((String)attributes.get("profile"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .nickname(name)
                .email(email)
                .profile(picture)
                .authorities(Collections.singleton(Authority.builder()
                        .authorityName("ROLE_USER")
                        .build()))
                .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("name", name);
        map.put("email", email);
        map.put("picture", picture);

        return map;
    }
}
