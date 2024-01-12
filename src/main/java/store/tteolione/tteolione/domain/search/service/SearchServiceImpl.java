package store.tteolione.tteolione.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import store.tteolione.tteolione.domain.product.service.ProductService;
import store.tteolione.tteolione.domain.search.dto.SearchProductResponse;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.service.UserService;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.exception.GeneralException;

import java.time.LocalDate;

import static org.springframework.util.StringUtils.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImpl implements SearchService {

    private final ProductService productService;
    private final UserService userService;

    @Override
    public SearchProductResponse searchProductList(String query, Double longitude, Double latitude, LocalDate searchStartDate, LocalDate searchEndDate, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByLoginId(authentication.getName());

        System.out.println("query = " + query);
        //null, ""
        if (!hasText(query)) {
            throw new GeneralException(Code.EMPTY_QUERY);
        }
        String keyword = query.replaceAll("\\s+", " ").trim();
        SearchProductResponse searchProductResponse = productService.searchProductList(user, keyword, longitude, latitude, searchStartDate, searchEndDate, pageable);

        return searchProductResponse;
    }
}
