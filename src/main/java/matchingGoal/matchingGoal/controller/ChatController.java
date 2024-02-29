package matchingGoal.matchingGoal.controller;

import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
  private final ChatService chatService;
  //TODO : 멤버 기능 완성시 userdetails로 호스트id 받아올것
  @PostMapping ("/personal")
  public ResponseEntity<?> createChatRoom(@RequestParam("hostId") long hostId, @RequestParam("guestId") long guestId) {

    chatService.createChatRoom(hostId, guestId);

    return null;

  }

}
