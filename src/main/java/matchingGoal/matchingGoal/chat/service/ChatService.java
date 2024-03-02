package matchingGoal.matchingGoal.chat.service;

import java.util.List;
import matchingGoal.matchingGoal.chat.entity.User;
import matchingGoal.matchingGoal.chat.dto.ChatRoomListResponse;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
  String createChatRoom(long hostId, long guestId);
  void addMembers(String chatRoomId, List<User> members);
  List<ChatRoomListResponse> myChat(long userId);
  void quit(long userIdString, String chatRoomId );
}
