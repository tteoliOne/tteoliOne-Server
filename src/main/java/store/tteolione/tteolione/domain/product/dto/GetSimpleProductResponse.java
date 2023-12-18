package store.tteolione.tteolione.domain.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetSimpleProductResponse {

    private List<CategoryProductDto> list;

    public GetSimpleProductResponse(List<CategoryProductDto> list) {
        this.list = list;
    }
}