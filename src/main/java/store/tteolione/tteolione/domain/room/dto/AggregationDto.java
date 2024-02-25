package store.tteolione.tteolione.domain.room.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AggregationDto implements Serializable {

    private Long productNo;
    private String isIncrease;
    private AggregationTarget target;
}
