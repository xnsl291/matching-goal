package matchingGoal.matchingGoal.service;

import java.util.List;
import matchingGoal.matchingGoal.domain.entity.User;
import matchingGoal.matchingGoal.dto.ChatRoomListResponse;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
  String createChatRoom(long hostId, long guestId);
  void addMembers(String chatRoomId, List<User> members);
  List<ChatRoomListResponse> myChat(long userId);
}
