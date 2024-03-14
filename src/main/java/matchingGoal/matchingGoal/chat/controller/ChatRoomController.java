package matchingGoal.matchingGoal.chat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.chat.dto.ChatMessageDto;
import matchingGoal.matchingGoal.chat.dto.ChatRoomListResponse;
import matchingGoal.matchingGoal.chat.service.ChatRoomService;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

  private final ChatRoomService chatRoomService;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/personal")
  public ResponseEntity<?> createChatRoom(@RequestHeader(value = "authorization") String token,
      @RequestParam("guest-id") long guestId) {
    long hostId = jwtTokenProvider.getId(token);
    String result = chatRoomService.createChatRoom(hostId, guestId);

    return ResponseEntity.ok(result);

  }

  @GetMapping("/list")
  public ResponseEntity<?> myChats(@RequestHeader(value = "authorization") String token) {
    long userId = jwtTokenProvider.getId(token);
    List<ChatRoomListResponse> result = chatRoomService.myChat(userId);

    return ResponseEntity.ok(result);
  }

  //채팅방 나가기
  @DeleteMapping("/{chatRoomId}")
  public ResponseEntity<?> quitChatRoom(@RequestHeader(value = "authoritzation") String token,
      @PathVariable String chatRoomId) {
    long userId = jwtTokenProvider.getId(token);
    chatRoomService.quit(userId, chatRoomId);

    return ResponseEntity.ok(null);
  }

  @GetMapping("/{chatRoomId}")
  public ResponseEntity<?> getChatMessage(@PathVariable String chatRoomId) {
    List<ChatMessageDto> result = chatRoomService.getChatMessage(chatRoomId);

    return ResponseEntity.ok(result);
  }
}
