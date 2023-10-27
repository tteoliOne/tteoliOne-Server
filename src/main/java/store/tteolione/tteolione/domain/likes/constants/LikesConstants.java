package store.tteolione.tteolione.domain.likes.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LikesConstants {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ELikeStatus {
        eLIKED(true),
        eNOT_LIKED(false);

        private boolean likeStatus;
    }

}
