package store.tteolione.tteolione.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SimpleProfileResponse {

    private String profile;
    private String nickname;
    private String intro;
    private double ddabongScore;
    private int newProductCount;
    private int soldOutProductCount;
    private int reviewCount;

}
