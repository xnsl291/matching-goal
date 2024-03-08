package matchingGoal.matchingGoal.chat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.chat.dto.ChatRoomListResponse;
import matchingGoal.matchingGoal.chat.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {
  private final ChatRoomService chatRoomService;
  //TODO : 멤버 기능 완성시 userdetails로 호스트id 받아올것
  @PostMapping ("/personal")
  public ResponseEntity<?> createChatRoom(@RequestParam("host-id") long hostId, @RequestParam("guest-id") long guestId) {

    String result = chatRoomService.createChatRoom(hostId, guestId);

    return ResponseEntity.ok(result);

  }

  @GetMapping("/list")
  //TODO : 멤버 기능 완성시 userdetails로 호스트id 받아올것
  public ResponseEntity<?> myChats(@RequestParam("user-id") long userId) {

    List<ChatRoomListResponse> result = chatRoomService.myChat(userId);

    return ResponseEntity.ok(result);
  }

  //채팅방 나가기
  @DeleteMapping
  public ResponseEntity<?> quitChatRoom(@RequestParam("chat-room-id") String chatRoomId) {

   return null;
  }

}
