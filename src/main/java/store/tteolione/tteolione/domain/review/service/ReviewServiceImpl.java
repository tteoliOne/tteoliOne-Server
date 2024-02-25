package store.tteolione.tteolione.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.review.dto.ReviewDto;
import store.tteolione.tteolione.domain.review.entity.Review;
import store.tteolione.tteolione.domain.review.repository.ReviewRepository;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.repository.UserRepository;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.exception.GeneralException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public void save(User seller, Review review) {

        //리뷰 저장
        reviewRepository.save(review);

        //판매자 따봉점수 최신화
        List<Review> findReviews = reviewRepository.findBySeller(seller);
        double score = 0.0;
        for (Review findReview : findReviews) {
            score += findReview.getDdabong();
        }

        double ddabongScoreAvg = score / findReviews.size();
        seller.setDdabongScore(ddabongScoreAvg);

    }

    @Override
    public boolean existsByProduct(Product product) {
        return reviewRepository.existsByProduct(product);
    }

    @Override
    public List<ReviewDto> listReview(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_USER));
        //판매자 따봉점수 최신화
        List<Review> findReviews = reviewRepository.findBySeller(user);
        List<ReviewDto> reviewDtos = findReviews.stream()
                .map(review -> new ReviewDto(review.getProduct().getProductId(), review.getReviewId(), review.getUser().getNickname(), review.getContent(), review.getDdabong(), review.getCreateAt(), review.getUpdateAt()))
                .collect(Collectors.toList());
        return reviewDtos;
    }
}
