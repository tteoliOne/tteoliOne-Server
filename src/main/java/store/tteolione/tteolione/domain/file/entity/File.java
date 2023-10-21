package store.tteolione.tteolione.domain.file.entity;

import lombok.*;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.global.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public static File toEntity(String fileUrl, Product product) {
        return File.builder()
                .fileUrl(fileUrl)
                .product(product)
                .build();
    }

}
