package matchingGoal.matchingGoal.service;

import java.util.List;
import matchingGoal.matchingGoal.domain.entity.ChatRoom;
import matchingGoal.matchingGoal.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
  public void createChatRoom(long hostId, long guestId);
  public void addMember(long chatRoomId, List<User> members);
}
