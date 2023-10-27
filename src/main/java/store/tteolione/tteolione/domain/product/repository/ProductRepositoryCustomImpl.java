package store.tteolione.tteolione.domain.product.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import store.tteolione.tteolione.domain.category.entity.Category;
import store.tteolione.tteolione.domain.file.entity.QFile;
import store.tteolione.tteolione.domain.product.dto.SimpleProductDto;

import java.util.List;

import static store.tteolione.tteolione.domain.likes.entity.QLikes.*;
import static store.tteolione.tteolione.domain.product.constants.ProductConstants.*;
import static store.tteolione.tteolione.domain.product.entity.QProduct.*;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SimpleProductDto> findSimpleDtoByProductsUserId(Long userId, Category category, double longitude, double latitude) {

        List<SimpleProductDto> result = jpaQueryFactory
                .select(Projections.constructor(SimpleProductDto.class,
                        product.productId,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(QFile.file.fileUrl)
                                        .from(QFile.file)
                                        .where(QFile.file.fileId.eq(
                                                JPAExpressions
                                                        .select(QFile.file.fileId.min())
                                                        .from(QFile.file)
                                                        .where(QFile.file.product.eq(product))
                                        ))
                                        .orderBy(QFile.file.updateAt.asc())
                                , "imageUrl"
                        ),
                        product.title,
                        product.sharePrice.divide(product.shareCount).as("unitPrice"),
                        calculateWalkingDistance(longitude, latitude).as("walkingDistance"),
                        calculateWalkingTime(longitude, latitude).as("walkingTime"),
                        product.totalCount,
                        likes.likeStatus.as("isLiked")
                ))
                .from(likes)
                .leftJoin(likes.product, product)
                .where(categoryEq(category), userIdEq(userId), productStatusEq(EProductSoldStatus.eNew))
                .orderBy(product.updateAt.desc())
                .limit(5)
                .fetch();
        return result;
    }

    private BooleanExpression productStatusEq(EProductSoldStatus soldStatus) {
        return soldStatus == null ? null : product.soldStatus.eq(soldStatus);
    }

    private BooleanExpression categoryEq(Category category) {
        return category == null ? null : product.category.eq(category);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : likes.user.userId.eq(userId);
    }

    private static NumberOperation<Double> calculateWalkingDistance(double longitude, double latitude) {
        return Expressions.numberOperation(
                Double.class,
                Ops.DIV,
                Expressions.stringTemplate("ST_Distance_Sphere({0}, {1})",
                        Expressions.stringTemplate("POINT({0}, {1})", longitude, latitude),
                        Expressions.stringTemplate("POINT({0}, {1})", product.longitude, product.latitude)
                ),
                Expressions.constant(1.0)
        );
    }

    private static NumberExpression<Integer> calculateWalkingTime(double longitude, double latitude) {
        double walkingSpeedMetersPerSecond = 1.4; // 도보 속도, 초속 1.4m 혹은 5km/h
        double walkingSpeedMetersPerMinute = walkingSpeedMetersPerSecond * 60; // 분당 도보 속도
        return calculateWalkingDistance(longitude, latitude).divide(walkingSpeedMetersPerMinute).intValue();
    }

}
