package matchingGoal.matchingGoal.chat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.chat.dto.ChatHistoryDto;
import matchingGoal.matchingGoal.chat.dto.ChatRoomListResponseDto;
import matchingGoal.matchingGoal.chat.dto.CreateChatRoomRequestDto;
import matchingGoal.matchingGoal.chat.service.ChatRoomService;
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

  @PostMapping("/personal")
  public ResponseEntity<String> createChatRoom(@RequestHeader(value = "authorization") String token,
      @RequestBody
      CreateChatRoomRequestDto request) {

    String result = chatRoomService.createChatRoom(token, request);

    return ResponseEntity.ok(result);
  }

  @GetMapping("/list")
  public ResponseEntity<List<ChatRoomListResponseDto>> myChats(
      @RequestHeader(value = "authorization") String token) {

    List<ChatRoomListResponseDto> result = chatRoomService.myChat(token);

    return ResponseEntity.ok(result);
  }

  //채팅방 나가기
  @DeleteMapping("/{chatRoomId}")
  public ResponseEntity<?> quitChatRoom(@RequestHeader(value = "authorization") String token,
      @PathVariable String chatRoomId) {

    chatRoomService.quit(token, chatRoomId);

    return ResponseEntity.ok(null);
  }

  @GetMapping("/{chatRoomId}")
  public ResponseEntity<ChatHistoryDto> chatHistory(@RequestHeader(value = "authorization") String token, @PathVariable String chatRoomId) {

    ChatHistoryDto result = chatRoomService.getChatHistory(token, chatRoomId);

    return ResponseEntity.ok(result);
  }
}
