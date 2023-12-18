package store.tteolione.tteolione.domain.product.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class ProductDto {
    private long productId;
    private String imageUrl;
    private String title;
    private int unitPrice;
    private double walkingDistance;
    private int walkingTime;
    private int totalLikes;
    private Long likeId;
    private boolean liked;

    public ProductDto(long productId, String imageUrl, String title, int unitPrice, double walkingDistance, int walkingTime, int totalLikes, Long likeId, boolean liked) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.unitPrice = unitPrice;
        this.walkingDistance = walkingDistance;
        this.walkingTime = walkingTime;
        this.totalLikes = totalLikes;
        this.likeId = likeId;
        this.liked = liked;
    }

}
