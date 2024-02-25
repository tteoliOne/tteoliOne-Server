package store.tteolione.tteolione.domain.likes.entity;

import lombok.*;
import store.tteolione.tteolione.domain.likes.constants.LikesConstants;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.global.entity.BaseTimeEntity;

import jakarta.persistence.*;

import static store.tteolione.tteolione.domain.likes.constants.LikesConstants.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Likes extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private boolean likeStatus;

    @Builder
    public static Likes toEntity(User user, Product product) {
        return Likes.builder()
                .user(user)
                .product(product)
                .likeStatus(ELikeStatus.eLIKED.isLikeStatus())
                .build();
    }

}
