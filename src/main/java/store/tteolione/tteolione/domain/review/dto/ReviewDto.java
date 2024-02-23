package store.tteolione.tteolione.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewDto {

    private Long productId;
    private Long reviewId;
    private String writer;
    private String content;
    private int ddabongScore;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
