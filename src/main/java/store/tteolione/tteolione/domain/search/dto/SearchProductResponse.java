package store.tteolione.tteolione.domain.search.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;
import store.tteolione.tteolione.domain.product.dto.ProductDto;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchProductResponse {
    private String q;
    private Slice<ProductDto> list;

    public SearchProductResponse(String q, Slice<ProductDto> list) {
        this.q = q;
        this.list = list;
    }
}
