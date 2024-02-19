package store.tteolione.tteolione.domain.room.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.tteolione.tteolione.domain.room.entity.redis.ChatRoom;
import store.tteolione.tteolione.domain.room.repository.ChatRoomRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public void connectChatRoom(Long chatRoomNo, String loginId) {
        //채팅방 입장시
        //연결할 채팅방번호와 로그인ID로 레디스 저장
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomNo(chatRoomNo)
                .loginId(loginId)
                .build();

        chatRoomRepository.save(chatRoom);
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
