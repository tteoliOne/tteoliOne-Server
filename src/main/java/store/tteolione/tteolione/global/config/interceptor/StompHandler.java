package store.tteolione.tteolione.global.config.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import store.tteolione.tteolione.domain.room.service.ChatRoomService;
import store.tteolione.tteolione.domain.room.service.ChatService;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.exception.GeneralException;
import store.tteolione.tteolione.global.jwt.TokenProvider;

import java.util.Objects;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("StompAccessor = {}", accessor);

        String loginId = getLoginId(getAccessToken(accessor));

        // Disconnect 명령이 아닐 때만 handleMessage 호출
        handleMessage(accessor.getCommand(), accessor, loginId);
        return ChannelInterceptor.super.preSend(message, channel);
    }

    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor, String loginId) {
        switch (stompCommand) {
            case CONNECT:
                connectToChatRoom(accessor, loginId);
                break;
            case SUBSCRIBE:
            case SEND:
                getLoginId(getAccessToken(accessor));
                break;
        }
    }

    private void connectToChatRoom(StompHeaderAccessor accessor, String loginId) {
        //채팅방 번호 가져오기
        Long chatRoomNo = getChatRoomNo(accessor);

        //Redis에 채팅방 입장 내역 저장(입장 처리) -> B가 입장할 때
        chatRoomService.connectChatRoom(chatRoomNo, loginId);

        //A가 입장해있고 B가 입장할 때 B가 없을때 A가 작성한 채팅들을 읽음 처리하기위해 몽고DB 수정
        chatService.updateAllRead(chatRoomNo, loginId);

        //채팅방에 접속중인 회원이 있는지 확인 -> B가 입장하려고 할떄 A가 있다면 채팅 리스트 갱신 필요(읽음 처리 된 것을 모르기 때문)
        boolean isConnected = chatRoomService.isConnected(chatRoomNo);

        if (isConnected) {
            chatService.inviteMessage(chatRoomNo, loginId);
        }
    }

    private Long getChatRoomNo(StompHeaderAccessor accessor) {
        Long chatRoomNo = Long.valueOf(
                Objects.requireNonNull(accessor.getFirstNativeHeader("chatRoomNo"))
        );
        return chatRoomNo;
    }

    private String getLoginId(String accessToken) {
        try {
            tokenProvider.validateToken(accessToken);
        } catch (Exception e) {
            throw new MessageDeliveryException("/pub/message");
        }
        String loginId = tokenProvider.getAuthentication(accessToken).getName();
        return loginId;
    }

    private String getAccessToken(StompHeaderAccessor accessor) {
        String accessToken = accessor.getFirstNativeHeader("Authorization");
        return accessToken;
    }
}
