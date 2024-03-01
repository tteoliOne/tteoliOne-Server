package store.tteolione.tteolione.domain.room.service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.tteolione.tteolione.domain.product.entity.QProduct;
import store.tteolione.tteolione.domain.room.dto.ChatRoomResponse;
import store.tteolione.tteolione.domain.room.entity.Chat;
import store.tteolione.tteolione.domain.room.entity.QChat;
import store.tteolione.tteolione.domain.user.entity.QUser;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.repository.UserRepository;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.exception.GeneralException;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatQueryService {

    private final JPAQueryFactory jpaQueryFactory;
    private final UserRepository userRepository;


    public List<ChatRoomResponse> getChattingList(Long userId, Long productNo) {

        List<ChatRoomResponse> result = jpaQueryFactory.select(Projections.constructor(ChatRoomResponse.class,
                        QChat.chat.chatId,
                        QChat.chat.createMember,
                        QChat.chat.joinMember,
                        QChat.chat.productNo,
                        QProduct.product.title,
                        QChat.chat.regDate,
                        Projections.constructor(ChatRoomResponse.Participant.class,
                                ExpressionUtils.as(
                                        JPAExpressions.select(QUser.user.nickname)
                                                .from(QUser.user)
                                                .where(QUser.user.userId.eq(
                                                        new CaseBuilder()
                                                                .when(QChat.chat.createMember.eq(userId)).then(QChat.chat.joinMember)
                                                                .otherwise(QChat.chat.createMember)
                                                ))
                                        , "nickname"),
                                ExpressionUtils.as(
                                        JPAExpressions.select(QUser.user.profile)
                                                .from(QUser.user)
                                                .where(QUser.user.userId.eq(
                                                        new CaseBuilder()
                                                                .when(QChat.chat.createMember.eq(userId)).then(QChat.chat.joinMember)
                                                                .otherwise(QChat.chat.createMember)
                                                )), "profile"))
                ))
                .from(QChat.chat)
                .join(QProduct.product).on(QProduct.product.productId.eq(QChat.chat.productNo))
                .where(QChat.chat.createMember.eq(userId).or(QChat.chat.joinMember.eq(userId)), productNoEq(productNo))
                .fetch();

        return result;
    }

    private BooleanExpression productNoEq(Long productNo) {
        return Objects.nonNull(productNo) ? QChat.chat.productNo.eq(productNo) : null;
    }

    public User getReceiverNumber(Long chatRoomNo, Long senderNo) {
        Chat chatroom = jpaQueryFactory.select(QChat.chat)
                .from(QChat.chat)
                .where(QChat.chat.chatId.eq(chatRoomNo))
                .fetchOne();

        Long userId = chatroom.getCreateMember().equals(senderNo) ?
                chatroom.getJoinMember() : chatroom.getCreateMember();

        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_USER));
    }
}
