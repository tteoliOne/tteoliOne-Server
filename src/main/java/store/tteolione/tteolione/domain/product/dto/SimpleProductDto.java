package store.tteolione.tteolione.domain.product.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class SimpleProductDto {
    private long productId;
    private String imageUrl;
    private int unitPrice;
    private double walkingDistance;
    private int walkingTime;
    private int totalLikes;
    private boolean isLiked;

    public SimpleProductDto(long productId, String imageUrl, int unitPrice, double walkingDistance, int walkingTime, int totalLikes, boolean isLiked) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.unitPrice = unitPrice;
        this.walkingDistance = walkingDistance;
        this.walkingTime = walkingTime;
        this.totalLikes = totalLikes;
        this.isLiked = isLiked;
    }
}
