package store.tteolione.tteolione.domain.review.entity;

import lombok.*;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.global.entity.BaseTimeEntity;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String content;

    private int ddabong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
