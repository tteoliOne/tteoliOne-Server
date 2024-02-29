package store.tteolione.tteolione.domain.room.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import store.tteolione.tteolione.domain.room.dto.ChatRoomResponse;
import store.tteolione.tteolione.domain.room.dto.ChattingHistoryResponse;
import store.tteolione.tteolione.domain.room.dto.CreateChatRoomRequest;
import store.tteolione.tteolione.domain.room.dto.chat.Message;
import store.tteolione.tteolione.domain.room.entity.Chat;
import store.tteolione.tteolione.domain.room.service.ChatRoomService;
import store.tteolione.tteolione.domain.room.service.ChatService;
import store.tteolione.tteolione.global.dto.BaseResponse;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    /**
     * 채팅방 개설
     */
    @PostMapping("/api/chatRoom")
    public BaseResponse<Chat> createChatRoom(@Valid @RequestBody CreateChatRoomRequest createChatRoomDto) {
        Chat chatRoom = chatService.createChatRoom(createChatRoomDto);
        return BaseResponse.of(chatRoom);
    }

    /**
     * 채팅내역 조회
     */
    @GetMapping("/api/chatRoom/{roomNo}")
    public BaseResponse<ChattingHistoryResponse> chattingList(@PathVariable("roomNo") Long roomNo) {
        ChattingHistoryResponse chattingList = chatService.getChattingList(roomNo);
        return BaseResponse.of(chattingList);
    }

    /**
     * 채팅방 리스트 조회
     */
    @GetMapping("/api/chatRoom")
    public BaseResponse<List<ChatRoomResponse>> chatRoomList(@RequestParam(value = "productNo", required = false) Long productNo) {
        List<ChatRoomResponse> chatRoomList = chatService.getChatList(productNo);
        return BaseResponse.of(chatRoomList);
    }

    /**
     * 메시지 전송
     */
    @MessageMapping("/message")
    public void sendMessage(@Valid Message message, @Header("Authorization") String accessToken) {
        chatService.sendMessage(message, accessToken);
    }

    /**
     * 메시지 전송 후 callback
     */
    @PostMapping("/api/chatRoom/notification")
    public BaseResponse<Message> sendSaveMessageAndNotification(@Valid @RequestBody Message message) throws IOException {
        Message savedMessage = chatService.sendSaveMessageAndNotification(message);
        return BaseResponse.of(savedMessage);
    }

    /**
     * 채팅방 나가기
     */
    @PutMapping("/api/chatRoom/{chatRoomId}")
    public BaseResponse<String> exitChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.disconnectChatRoom(chatRoomId);
        return BaseResponse.of("정상적으로 채팅방을 나갔습니다.");
    }

    /**
     * 채팅방 떠나기
     */
    @DeleteMapping("/api/chatRoom/{chatRoomId}")
    public BaseResponse<String> deleteChatRoom(@PathVariable Long chatRoomId) {
        chatService.deleteChatRoom(chatRoomId);
        return BaseResponse.of("정상적으로 채팅방을 떠났습니다.");
    }
}
