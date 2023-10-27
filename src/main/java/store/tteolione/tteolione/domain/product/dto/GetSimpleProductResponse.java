package store.tteolione.tteolione.domain.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetSimpleProductResponse {

    private List<CategorySimpleProductDto> list;

    public GetSimpleProductResponse(List<CategorySimpleProductDto> list) {
        this.list = list;
    }
}