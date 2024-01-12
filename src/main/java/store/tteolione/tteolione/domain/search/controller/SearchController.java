package store.tteolione.tteolione.domain.search.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.tteolione.tteolione.domain.search.dto.SearchProductResponse;
import store.tteolione.tteolione.domain.search.service.SearchService;
import store.tteolione.tteolione.global.dto.BaseResponse;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("")
    //검색어
    //경위도
    //페이지

    //무한스크롤처리
    //
    public BaseResponse<SearchProductResponse> searchProduct(@RequestParam(name = "q") String query,
                                                                   @RequestParam Double longitude,
                                                                   @RequestParam Double latitude,
                                                                   @RequestParam(required = false, defaultValue = "19000101") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate searchStartDate,
                                                                   @RequestParam(required = false, defaultValue = "99991231") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate searchEndDate,
                                                                   Pageable pageable) {
        SearchProductResponse searchProductResponse = searchService.searchProductList(query, longitude, latitude, searchStartDate, searchEndDate, pageable);
        return BaseResponse.of(searchProductResponse);
    }
}
