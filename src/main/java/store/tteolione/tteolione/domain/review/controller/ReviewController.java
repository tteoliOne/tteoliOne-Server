package store.tteolione.tteolione.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.tteolione.tteolione.domain.review.dto.ReviewDto;
import store.tteolione.tteolione.domain.review.service.ReviewService;
import store.tteolione.tteolione.global.dto.BaseResponse;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{userId}")
    public BaseResponse<List<ReviewDto>> listReview(@PathVariable("userId") Long userId) {
        List<ReviewDto> reviewDtos = reviewService.listReview(userId);
        return BaseResponse.of(reviewDtos);
    }
}
