package store.tteolione.tteolione.domain.product.entity;

import lombok.*;
import store.tteolione.tteolione.domain.user.entity.User;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductTrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productTradeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    public ProductTrade(Product product, User seller, User buyer) {
        this.product = product;
        this.seller = seller;
        this.buyer = buyer;
    }
}
