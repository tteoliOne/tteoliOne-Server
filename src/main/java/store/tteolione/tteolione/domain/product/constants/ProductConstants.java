package store.tteolione.tteolione.domain.product.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductConstants {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum EProductSoldStatus{
        eNew("새로운 상품"),
        eReservation("예약중인 상품"),
        eSoldOut("판매 완료");

        private String name;
    }
}
