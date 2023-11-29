package store.tteolione.tteolione.domain.product.entity;

import lombok.*;
import store.tteolione.tteolione.domain.category.entity.Category;
import store.tteolione.tteolione.domain.file.entity.File;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static store.tteolione.tteolione.domain.product.constants.ProductConstants.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;


    private String title;
    private String description;

    private int buyPrice;
    private int buyCount;
    private int sharePrice;
    private int shareCount;
    private int totalCount;
    private LocalDateTime buyDate;
    private Integer likeCount;

    private double longitude; //경도
    private double latitude; //위도

    @Enumerated(EnumType.STRING)
    private EProductSoldStatus soldStatus;

    @OneToMany(mappedBy = "product")
    private List<File> images = new ArrayList<>(); //마지막 인덱스가 영수증사진

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String title, int buyPrice, int buyCount, int sharePrice, int shareCount, LocalDateTime buyDate, String description, double longitude, double latitude, User user, Category category) {
        this.title = title;
        this.buyPrice = buyPrice;
        this.buyCount = buyCount;
        this.sharePrice = sharePrice;
        this.shareCount = shareCount;
        this.buyDate = buyDate;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.user = user;
        this.category = category;
        this.likeCount = 0;
    }

    //연관관계 메서드
    public void addPhotos(List<File> images) {
        for (File image : images) {
            image.setProduct(this);
            this.getImages().add(image);
        }
    }

    public void modifyImages(List<File> images) {
        this.getImages().clear();
        for (File image : images) {
            image.setProduct(this);
            this.getImages().add(image);
        }
    }
}
