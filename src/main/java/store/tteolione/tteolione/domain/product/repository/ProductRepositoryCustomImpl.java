package store.tteolione.tteolione.domain.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import store.tteolione.tteolione.domain.category.entity.Category;
import store.tteolione.tteolione.domain.category.entity.QCategory;
import store.tteolione.tteolione.domain.file.entity.QFile;
import store.tteolione.tteolione.domain.product.constants.ProductConstants;
import store.tteolione.tteolione.domain.product.dto.ProductDto;
import store.tteolione.tteolione.domain.search.dto.SearchProductResponse;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.global.util.QuerydslUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;
import static store.tteolione.tteolione.domain.likes.entity.QLikes.*;
import static store.tteolione.tteolione.domain.product.constants.ProductConstants.*;
import static store.tteolione.tteolione.domain.product.entity.QProduct.*;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ProductDto> findSimpleDtoByProductsUserId(User user, Category category, double longitude, double latitude) {
        List<ProductDto> result = jpaQueryFactory
                .select(Projections.constructor(ProductDto.class,
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
                        product.likeCount.as("totalLikes"),
                        product.soldStatus.as("soldStatus"),
                        likes.likeId,
                        likes.likeStatus.as("liked")
                ))
                .from(product)
                .leftJoin(likes)
                .on(likes.product.eq(product).and(likes.user.eq(user)))
                .where(categoryEq(category), productStatusEq("eNew"), product.status.eq("A"))
                .orderBy(product.createAt.desc())
                .limit(5)
                .fetch();
        return result;
    }


    @Override
    public Slice<ProductDto> findListProductDtoByProducts(Category category, User user, double longitude, double latitude, LocalDate searchStartDate, LocalDate searchEndDate, Pageable pageable) {
        List<OrderSpecifier> ORDERS = productSort(pageable);
        QueryResults<ProductDto> result = jpaQueryFactory
                .select(Projections.constructor(ProductDto.class,
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
                        product.likeCount.as("totalLikes"),
                        product.soldStatus.as("soldStatus"),
                        likes.likeId,
                        likes.likeStatus.as("liked")
                ))
                .from(product)
                .leftJoin(likes)
                .on(likes.product.eq(product).and(likes.user.eq(user)))
                .where(product.status.eq("A"), categoryEq(category), productStatusEq("eNew"), searchDateBetween(searchStartDate, searchEndDate))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .fetchResults();

        List<ProductDto> content = new ArrayList<>();
        for (ProductDto eachProduct : result.getResults()) {
            content.add(eachProduct);
        }

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Slice<ProductDto> findMyListProductDtoByProducts(User user, double longitude, double latitude, String soldStatus, Pageable pageable) {
        List<OrderSpecifier> ORDERS = productSort(pageable);
        QueryResults<ProductDto> result = jpaQueryFactory
                .select(Projections.constructor(ProductDto.class,
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
                        product.likeCount.as("totalLikes"),
                        product.soldStatus.as("soldStatus"),
                        likes.likeId,
                        likes.likeStatus.as("liked")
                ))
                .from(product)
                .leftJoin(likes)
                .on(likes.product.eq(product).and(likes.user.eq(user)))
                .where(product.status.eq("A"), product.user.eq(user), productStatusEq(soldStatus))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .fetchResults();

        List<ProductDto> content = new ArrayList<>();
        for (ProductDto eachProduct : result.getResults()) {
            content.add(eachProduct);
        }

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Slice<ProductDto> findOpponentListProductDtoByProducts(double longitude, double latitude, User user, User opponent, String soldStatus, Pageable pageable) {
        QueryResults<ProductDto> result = jpaQueryFactory
                .select(Projections.constructor(ProductDto.class,
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
                        product.likeCount.as("totalLikes"),
                        product.soldStatus.as("soldStatus"),
                        likes.likeId,
                        likes.likeStatus.as("liked")
                ))
                .from(product)
                .leftJoin(likes)
                .on(likes.product.eq(product).and(likes.user.eq(user)))
                .where(product.status.eq("A"), product.user.eq(opponent), productStatusEq(soldStatus))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(product.createAt.desc())
                .fetchResults();

        List<ProductDto> content = new ArrayList<>();
        for (ProductDto eachProduct : result.getResults()) {
            content.add(eachProduct);
        }

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Slice<ProductDto> searchProductByCondition(User user, String keyword, Double longitude, Double latitude, LocalDate searchStartDate, LocalDate searchEndDate, Pageable pageable) {
        List<OrderSpecifier> ORDERS = productSort(pageable);
        QueryResults<ProductDto> result = jpaQueryFactory
                .select(Projections.constructor(ProductDto.class,
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
                        product.likeCount.as("totalLikes"),
                        product.soldStatus.as("soldStatus"),
                        likes.likeId,
                        likes.likeStatus.as("liked")
                ))
                .from(product)
                .join(product.category, QCategory.category)
                .leftJoin(likes)
                .on(likes.product.eq(product).and(likes.user.eq(user)))
                .where(product.status.eq("A"), productStatusEq("eNew"), searchDateBetween(searchStartDate, searchEndDate), product.title.like("%"+keyword+"%").or(QCategory.category.categoryName.like("%"+keyword+"%")))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .fetchResults();

        List<ProductDto> content = new ArrayList<>();
        for (ProductDto eachProduct : result.getResults()) {
            content.add(eachProduct);
        }

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    /**
     * 조건
     */
    private BooleanExpression productStatusEq(String soldStatus) {
        if (StringUtils.hasText(soldStatus)) {
            // 백스페이스 제거
            soldStatus = soldStatus.replaceAll("\b", "");
            soldStatus = soldStatus.trim(); // 문자열 양 끝의 공백 제거
            if (soldStatus.equals("eNew") || soldStatus.equals("eSoldOut")) {
                return product.soldStatus.eq(EProductSoldStatus.valueOf(soldStatus));
            } else {
                return null;
            }
        }
        return null;
    }

    private BooleanExpression categoryEq(Category category) {
        return category == null ? null : product.category.eq(category);
    }

    private BooleanExpression searchDateBetween(LocalDate searchStartDate, LocalDate searchEndDate) {
        return dateGoe(searchStartDate).and(dateLoe(searchEndDate));
    }

    private BooleanExpression dateGoe(LocalDate searchStartDate) {
        return product.createAt.goe(LocalDateTime.of(searchStartDate, LocalTime.MIN));
    }

    private BooleanExpression dateLoe(LocalDate searchEndDate) {
        return product.createAt.loe(LocalDateTime.of(searchEndDate, LocalTime.MAX).withNano(0));
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

    private List<OrderSpecifier> productSort(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort()) && pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
//                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "createAt-asc":
                        OrderSpecifier<?> createAtAsc = QuerydslUtil.getSortedColumn(Order.ASC, product, "createAt");
                        ORDERS.add(createAtAsc);
                        break;
                    case "createAt-desc":
                        OrderSpecifier<?> creaetAtDesc = QuerydslUtil.getSortedColumn(Order.DESC, product, "createAt");
                        ORDERS.add(creaetAtDesc);
                        break;
                    case "updateAt-asc":
                        OrderSpecifier<?> updateAtAsc = QuerydslUtil.getSortedColumn(Order.ASC, product, "updateAt");
                        ORDERS.add(updateAtAsc);
                        break;
                    case "updateAt-desc":
                        OrderSpecifier<?> updateAtDesc = QuerydslUtil.getSortedColumn(Order.DESC, product, "updateAt");
                        ORDERS.add(updateAtDesc);
                        break;
                    default:
                        break;
                }
            }
        } else {
            OrderSpecifier<?> creaetAtDesc = QuerydslUtil.getSortedColumn(Order.DESC, product, "createAt");
            ORDERS.add(creaetAtDesc);
        }

        return ORDERS;
    }

    //Like
    private BooleanBuilder buildLikeConditions(String[] words) {
        String keyword = String.join("%", words);
        BooleanBuilder likeConditions = new BooleanBuilder();
        likeConditions.or(product.title.contains(keyword)
                .or(QCategory.category.categoryName.contains(keyword)));


        return likeConditions;
    }




}