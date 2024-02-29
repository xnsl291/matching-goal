package matchingGoal.matchingGoal.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.domain.entity.ChatRoom;
import matchingGoal.matchingGoal.domain.entity.User;
import matchingGoal.matchingGoal.repository.ChatRoomRepository;
import matchingGoal.matchingGoal.service.ChatService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

  private final ChatRoomRepository chatRoomRepository;

  @Override
  public void createChatRoom(long hostId, long guestId) {
    User host = null;
    User guest = null;
    List<User> members = new ArrayList<>();

    members.add(host);
    members.add(guest);

    ChatRoom room = ChatRoom.create();
    room.addMembers(members);
  }

  @Override
  public void addMember(long chatRoomId, List<User> members) {
    ChatRoom room = chatRoomRepository.findById(chatRoomId).orElseThrow(RuntimeException::new);
    room.addMembers(members);
  }


}
