package store.tteolione.tteolione.domain.product.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class SimpleProductDto {
    private long productId;
    private String imageUrl;
    private String title;
    private int unitPrice;
    private double walkingDistance;
    private int walkingTime;
    private int totalLikes;
    private boolean liked;

    public SimpleProductDto(long productId, String imageUrl, String title, int unitPrice, double walkingDistance, int walkingTime, int totalLikes, boolean liked) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.unitPrice = unitPrice;
        this.walkingDistance = walkingDistance;
        this.walkingTime = walkingTime;
        this.totalLikes = totalLikes;
        this.liked = liked;
    }
}
