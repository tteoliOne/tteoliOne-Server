package store.tteolione.tteolione.domain.search.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import store.tteolione.tteolione.domain.search.dto.SearchProductResponse;

import java.time.LocalDate;

public interface SearchService {
    SearchProductResponse searchProductList(String query, Double longitude, Double latitude, LocalDate searchStartDate, LocalDate searchEndDate, Pageable pageable);
}
