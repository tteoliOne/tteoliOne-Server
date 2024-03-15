package store.tteolione.tteolione.domain.product.dto;

import lombok.*;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;

@Data
@NoArgsConstructor
public class ProductDto {
    private Long productId;
    private String imageUrl;
    private String title;
    private int unitPrice;
    private double walkingDistance;
    private int walkingTime;
    private int totalLikes;
    private String soldStatus;
    private Long likeId;
    private boolean liked;
    private Double longitude;
    private Double latitude;

    public ProductDto(long productId, String imageUrl, String title, int unitPrice, double walkingDistance, int walkingTime, int totalLikes, ProductConstants.EProductSoldStatus soldStatus, Long likeId, boolean liked) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.unitPrice = unitPrice;
        this.walkingDistance = walkingDistance;
        this.walkingTime = walkingTime;
        this.totalLikes = totalLikes;
        this.soldStatus = soldStatus.toString();
        this.likeId = likeId;
        this.liked = liked;
    }

    public ProductDto(Long productId, String imageUrl, String title, int unitPrice, int totalLikes, ProductConstants.EProductSoldStatus soldStatus, Double longitude, Double latitude) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.unitPrice = unitPrice;
        this.totalLikes = totalLikes;
        this.soldStatus = soldStatus.toString();
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
