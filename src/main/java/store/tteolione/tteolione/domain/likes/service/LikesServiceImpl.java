package store.tteolione.tteolione.domain.likes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.tteolione.tteolione.domain.likes.constants.LikesConstants;
import store.tteolione.tteolione.domain.likes.entity.Likes;
import store.tteolione.tteolione.domain.likes.repository.LikesRepository;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.product.service.ProductService;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.service.UserService;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likesRepository;

    @Override
    public Likes modifyLike(User user, Product product) {
        Likes findLike = likesRepository.findByUserAndProduct(user, product)
                .orElseGet(() -> likesRepository.save(Likes.toEntity(user, product)));

        findLike.toggleLike();
        return findLike;
    }

    @Override
    public int getTotalLikes(Product product) {
        System.out.println(LikesConstants.ELikeStatus.eLIKED.isLikeStatus());
        return likesRepository.countByProductAndLikeStatus(product, LikesConstants.ELikeStatus.eLIKED.isLikeStatus());
    }
}
