package matchingGoal.matchingGoal.chat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.chat.dto.ChatHistoryDto;
import matchingGoal.matchingGoal.chat.dto.ChatRoomListResponseDto;
import matchingGoal.matchingGoal.chat.dto.CreateChatRoomRequestDto;
import matchingGoal.matchingGoal.chat.service.ChatRoomService;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

  private final ChatRoomService chatRoomService;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/personal")
  public ResponseEntity<String> createChatRoom(@RequestHeader(value = "authorization") String token,
      @RequestBody
      CreateChatRoomRequestDto request) {

    token = token.substring(7);
    long hostId = jwtTokenProvider.getId(token);
    String result = chatRoomService.createChatRoom(hostId, request.getMemberId());

    return ResponseEntity.ok(result);
  }

  @GetMapping("/list")
  public ResponseEntity<List<ChatRoomListResponseDto>> myChats(
      @RequestHeader(value = "authorization") String token) {

    token = token.substring(7);
    long userId = jwtTokenProvider.getId(token);

    List<ChatRoomListResponseDto> result = chatRoomService.myChat(userId);

    return ResponseEntity.ok(result);
  }

  //채팅방 나가기
  @DeleteMapping("/{chatRoomId}")
  public ResponseEntity<?> quitChatRoom(@RequestHeader(value = "authorization") String token,
      @PathVariable String chatRoomId) {

    long userId = jwtTokenProvider.getId(token);

    chatRoomService.quit(userId, chatRoomId);

    return ResponseEntity.ok(null);
  }

  @GetMapping("/{chatRoomId}")
  public ResponseEntity<ChatHistoryDto> chatHistory(@PathVariable String chatRoomId) {

    ChatHistoryDto result = chatRoomService.getChatHistory(chatRoomId);

    return ResponseEntity.ok(result);
  }
}
