package store.tteolione.tteolione.domain.likes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.tteolione.tteolione.domain.likes.constants.LikesConstants;
import store.tteolione.tteolione.domain.likes.entity.Likes;
import store.tteolione.tteolione.domain.likes.repository.LikesRepository;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.product.service.ProductService;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likesRepository;


    @Override
    public Optional<Likes> findByProductAndUser(Product product, User user) {
        return likesRepository.findByProductAndUser(product, user);
    }

    @Override
    public void createLikes(Likes likes) {
        likesRepository.save(likes);
    }

    @Override
    public void deleteLikes(Likes likes) {
        likesRepository.delete(likes);
    }
}
