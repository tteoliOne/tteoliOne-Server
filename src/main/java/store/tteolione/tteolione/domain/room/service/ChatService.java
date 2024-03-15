package store.tteolione.tteolione.domain.room.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.tteolione.tteolione.domain.notification.service.NotificationService;
import store.tteolione.tteolione.domain.product.entity.Product;
import store.tteolione.tteolione.domain.product.repository.ProductRepository;
import store.tteolione.tteolione.domain.product.repository.ProductTradeRepository;
import store.tteolione.tteolione.domain.review.repository.ReviewRepository;
import store.tteolione.tteolione.domain.room.dto.*;
import store.tteolione.tteolione.domain.room.dto.chat.Message;
import store.tteolione.tteolione.domain.room.entity.Chat;
import store.tteolione.tteolione.domain.room.entity.mongodb.Chatting;
import store.tteolione.tteolione.domain.room.repository.ChatRepository;
import store.tteolione.tteolione.domain.room.repository.mongodb.MongoChatRepository;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.repository.UserRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.exception.GeneralException;
import store.tteolione.tteolione.global.jwt.TokenProvider;
import store.tteolione.tteolione.global.util.ConstantUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    private final MessageSender sender;
    private final ChatRepository chatRepository;
    private final MongoChatRepository mongoChatRepository;
    private final ChatQueryService chatQueryService;
    private final AggregationSender aggregationSender;
    private final TokenProvider tokenProvider;
    private final ChatRoomService chatRoomService;
    private final NotificationService notificationService;
    private final ProductRepository productRepository;
    private final ProductTradeRepository productTradeRepository;
    private final ReviewRepository reviewRepository;


    public Chat createChatRoom(CreateChatRoomRequest createChatRoomDto) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        User findUser = userRepository.findByLoginId(loginId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_USER));
        Product findProduct = productRepository.findById(createChatRoomDto.getProductNo()).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_PRODUCT));

        if (findUser.getUserId().equals(findProduct.getUser().getUserId())) {
            throw new GeneralException(Code.EQUAL_SELLER_BUYER);
        }


        Optional<Chat> _findChat = chatRepository.findByProductNoAndCreateMember(findProduct.getProductId(), findUser.getUserId());
        if (_findChat.isPresent()) {
            Chat findChat = _findChat.get();

            //구매자가 떠났거나 판매자가 떠났을 때
            if (findChat.isExitCreateMember() || findChat.isExitJoinMember()) {
                //기존 채팅방 삭제
                chatRepository.delete(findChat);
            }
            //구매자와 판매자 모두가 떠나지 않았을 때
            if (!findChat.isExitCreateMember() && !findChat.isExitJoinMember()) {
                return findChat;
            }

        }


        Chat chat = Chat.builder()
                .productNo(findProduct.getProductId())
                .createMember(findUser.getUserId())
                .joinMember(findProduct.getUser().getUserId())
                .regDate(LocalDateTime.now())
                .exitCreateMember(false)
                .exitJoinMember(false)
                .build();

        Chat savedChat = chatRepository.save(chat);


        AggregationDto aggregationDto = AggregationDto.builder()
                .isIncrease("true")
                .target(AggregationTarget.CHAT)
                .productNo(createChatRoomDto.getProductNo())
                .build();

        aggregationSender.send(ConstantUtils.KAFKA_AGGREGATION, aggregationDto);
        return savedChat;
    }

    // 읽지 않은 메시지 채팅장 입장시 읽음 처리
    public void updateAllRead(Long chatRoomNo, String loginId) {
        User findUser = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_USER));

        Update update = new Update().set("readCount", 0);
        Query query = new Query(Criteria.where("chatRoomNo").is(chatRoomNo)
                .and("senderNo").ne(findUser.getUserId()));

        mongoTemplate.updateMulti(query, update, Chatting.class);
    }

    public void inviteMessage(Long chatRoomNo, String loginId) {
        Message inviteMessage = Message.builder()
                .contentType("notice")
                .chatRoomNo(chatRoomNo)
                .content(loginId + "님이 돌아오셨습니다.")
                .build();

        sender.send(ConstantUtils.KAFKA_TOPIC, inviteMessage);
    }


    public ChattingHistoryResponse getChattingList(Long chatRoomNo) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_USER));

        //채팅내역 조회니까 다른 사람이 보낸 것을 읽음처리해야됨
        updateAllRead(chatRoomNo, user.getLoginId());

        List<ChatResponse> chattingList = new ArrayList<>();
        List<Chatting> chatList = mongoChatRepository.findByChatRoomNo(chatRoomNo);

        //채팅기록
        for (Chatting chatting : chatList) {
            ChatResponse chatResponse = new ChatResponse(chatting, user.getUserId());
            chattingList.add(chatResponse);
        }

        Chat findChatRoom = chatRepository.findByChatId(chatRoomNo).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_CHAT_ROOM));
        Long productId = findChatRoom.getProductNo();
        Product findProduct = productRepository.findByUserAndProductId(productId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_PRODUCT));
        User opponentUser;
        // 자신이 상품을 공유하는 사람이면 -> 채팅을 개설한 사람 닉네임 전송(opponentNickname)
        if (user.getUserId().equals(findProduct.getUser().getUserId())) {
            opponentUser = userRepository.findById(findChatRoom.getCreateMember()).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_USER));
        } else {
            //상품을 공유하는 사람(opponentNickname)
            opponentUser = findProduct.getUser();
        }

        //자신이 판매자인지
        boolean checkSeller = findProduct.getUser().getUserId().equals(user.getUserId()); //판매자가 JWT 토큰과 일치할때

        boolean checkReservation = checkSeller ?
                productTradeRepository.existsByProductAndBuyer(findProduct, opponentUser) : productTradeRepository.existsByProductAndBuyer(findProduct, user);

        boolean checkReview = reviewRepository.existsByProduct(findProduct);

        return ChattingHistoryResponse.builder()
                .loginId(user.getLoginId())
                .productId(findProduct.getProductId())
                .title(findProduct.getTitle())
                .productImage(findProduct.productProfile())
                .sharePrice(findProduct.getSharePrice())
                .opponentId(opponentUser.getUserId())
                .opponentNickname(opponentUser.getNickname())
                .opponentProfile(opponentUser.getProfile())
                .exitOpponent(checkSeller ? findChatRoom.isExitCreateMember() : findChatRoom.isExitJoinMember())
                .soldStatus(findProduct.getSoldStatus())
                .checkSeller(checkSeller)
                .checkReservation(checkReservation)
                .checkReview(checkReview)
                .chatList(chattingList)
                .build();
    }

    public List<ChatRoomResponse> getChatList(Long productNo) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        User findUser = userRepository.findByLoginId(loginId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_USER));

        List<ChatRoomResponse> chatRoomList = chatQueryService.getChattingList(findUser.getUserId(), productNo);

        chatRoomList
                .forEach(chatRoomDto -> {
                    // 채팅방별로 읽지 않은 메시지 개수를 셋팅
                    long unReadCount = countUnReadMessages(chatRoomDto.getChatNo(), findUser.getUserId());
                    chatRoomDto.setUnReadCount(unReadCount);

                    // 채팅방별로 마지막 채팅내용과 시간을 셋팅
                    Page<Chatting> chatting =
                            mongoChatRepository.findByChatRoomNoOrderBySendDateDesc(chatRoomDto.getChatNo(), PageRequest.of(0, 1));
                    if (chatting.hasContent()) {
                        Chatting chat = chatting.getContent().get(0);
                        ChatRoomResponse.LatestMessage latestMessage = ChatRoomResponse.LatestMessage.builder()
                                .context(chat.getContent())
                                .sendAt(chat.getSendDate())
                                .build();
                        chatRoomDto.setLatestMessage(latestMessage);
                    }
                });

        return chatRoomList;
    }

    private long countUnReadMessages(Long chatNo, Long senderNo) {

        Query query = new Query(Criteria.where("chatRoomNo").is(chatNo)
                .and("readCount").is(1)
                .and("senderNo").ne(senderNo));

        return mongoTemplate.count(query, Chatting.class);
    }

    public void sendMessage(Message message, String accessToken) {
        // 메시지 전송 요청 헤더에 포함된 accessToken에서 loginId를 가져와 회원 조회
        String loginId = tokenProvider.getAuthentication(accessToken).getName();
        User findUser = userRepository.findByLoginId(loginId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_USER));

        //채팅방에 모든 유저가 참여중인지 체크
        boolean isConnectedAll = chatRoomService.isAllConnected(message.getChatRoomNo());

        // 1:1 채팅으로 2명 접속시 readCount 0, 한명만 접속시 readCount 1
        Integer readCount = isConnectedAll ? 0 : 1;

        //message 객체에 보낸시간, 보낸사람 userId, 닉네임 세팅
        message.setSendTimeAndSender(LocalDateTime.now(), findUser.getUserId(), findUser.getNickname(), findUser.getLoginId(), readCount);

        //메시지 전송
        sender.send(ConstantUtils.KAFKA_TOPIC, message);
    }

    public Message sendSaveMessageAndNotification(Message message) throws IOException {
        // 메세지 저장과 알림 발송을 위해 메세지를 보낸 회원을 조회
        User findUser = userRepository.findById(message.getSenderNo()).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_USER));
        Product findProduct = productRepository.findById(message.getProductNo()).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_PRODUCT));

        Chat chatRoom = chatRepository.findByChatId(message.getChatRoomNo()).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_CHAT_ROOM));
        //상대방이 읽지 않은 경우에만 알림 전송
        if (message.getReadCount().equals(1)) {
            User receiveUser = chatQueryService.getReceiverNumber(message.getChatRoomNo(), message.getSenderNo());
            String content = message.getContentType().equals("image") ? "image" : message.getContent();
            //메시지 받는 유저가 구매자인데 떠난 상태가 아닐 때
            if (receiveUser.getUserId().equals(chatRoom.getCreateMember()) && !chatRoom.isExitCreateMember()) {
                notificationService.sendMessageTo(receiveUser.getTargetToken(), findProduct.getTitle(), findUser.getNickname() + " : " + content, "hello", "hi");
            }
            //메시지 받는 유저가 판매자인데 떠난 상태가 아닐 때
            if (receiveUser.getUserId().equals(chatRoom.getJoinMember()) && !chatRoom.isExitJoinMember()) {
                notificationService.sendMessageTo(receiveUser.getTargetToken(), findProduct.getTitle(), findUser.getNickname() + " : " + content, "hello", "hi");
            }
        }

        // 보낸 사람일 경우에만 메시지를 저장 -> 중복 저장 방지
        if (message.getSenderLoginId().equals(findUser.getLoginId())) {
            // Message 객체를 채팅 엔티티로 변환한다.
            Chatting chatting = message.toEntity();
            // 채팅 내용을 저장한다.
            Chatting savedChat = mongoChatRepository.save(chatting);
            // 저장된 고유 ID를 반환한다.
            message.setId(savedChat.getId());
        }

        return message;
    }


    public void deleteChatRoom(Long chatRoomId) {

        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_USER));

        //채팅방 레디스 삭제
        chatRoomService.disconnectChatRoom(chatRoomId);

        //채팅방
        Chat findChatRoom = chatRepository.findByChatId(chatRoomId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_CHAT_ROOM));
        Long productId = findChatRoom.getProductNo();
        Product product = productRepository.findByUserAndProductId(productId).orElseThrow(() -> new GeneralException(Code.NOT_EXISTS_PRODUCT));

        //자신이 판매자인지
        boolean checkSeller = product.getUser().getUserId().equals(user.getUserId()); //판매자가 JWT토큰과 일치할때

        // -- 상대방이 구독이 안되어있을 때, 즉 채팅방을 나가있을때 떠난 것을 알 수가 없어 DB에 저장 -- //
        //떠나는 사람이 판매자일때
        if (checkSeller) {
            findChatRoom.exitJoinMember();
        } else {
            //떠나는 사람이 구매자일때
            findChatRoom.exitCreateMember();
        }

        Message message = Message.builder()
                .chatRoomNo(chatRoomId)
                .productNo(findChatRoom.getProductNo())
                .contentType("notice")
                .content("상대방이 채팅방을 나갔습니다.")
                .build();
        message.setSendTimeAndSender(LocalDateTime.now(), user.getUserId(), user.getNickname(), user.getLoginId(), 0);

        // Message 객체를 채팅 엔티티로 변환한다.
        Chatting chatting = message.toEntity();
        // 채팅 내용을 저장한다.
        Chatting savedChat = mongoChatRepository.save(chatting);
        // 저장된 고유 ID를 반환한다.
        message.setId(savedChat.getId());

        //메시지 전송
        sender.send(ConstantUtils.KAFKA_TOPIC, message);

    }

    public void productTradeSend(Long chatRoomId, String type) {
        // -- 상대방이 현재 채팅방에 입장해있을때, 바로 보내게 하기 위함 -- //
        //message 객체에 보낸시간, 보낸사람 userId, 닉네임 세팅
        Message message = Message.builder()
                .chatRoomNo(chatRoomId)
                .contentType(type)
                .content("거래 상태 변경되었습니다.")
                .build();

        //메시지 전송
        sender.send(ConstantUtils.KAFKA_TOPIC, message);
    }
}
