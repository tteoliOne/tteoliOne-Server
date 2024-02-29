package store.tteolione.tteolione.domain.room.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.tteolione.tteolione.domain.room.entity.redis.ChatRoom;
import store.tteolione.tteolione.domain.room.repository.ChatRoomRepository;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.repository.UserRepository;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.exception.GeneralException;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public void connectChatRoom(Long chatRoomNo, String loginId) {
        //채팅방 입장시
        //연결할 채팅방번호와 로그인ID로 레디스 저장
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomNo(chatRoomNo)
                .loginId(loginId)
                .build();

        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void disconnectChatRoom(Long chatRoomNo) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        ChatRoom chatRoom = chatRoomRepository.findByChatRoomNoAndLoginId(chatRoomNo, loginId)
                .orElseThrow(IllegalStateException::new);

        chatRoomRepository.delete(chatRoom);
    }

    public boolean isConnected(Long chatRoomNo) {
        List<ChatRoom> connectedList = chatRoomRepository.findByChatRoomNo(chatRoomNo);
        boolean isConnected = connectedList.size() == 1;
        return isConnected;
    }

    public boolean isAllConnected(Long chatRoomNo) {
        List<ChatRoom> connectedList = chatRoomRepository.findByChatRoomNo(chatRoomNo);
        boolean isAllConnected = connectedList.size() == 2;
        return isAllConnected;
    }
}
