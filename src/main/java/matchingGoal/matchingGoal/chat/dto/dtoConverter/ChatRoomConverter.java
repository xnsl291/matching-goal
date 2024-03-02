package matchingGoal.matchingGoal.chat.dto.dtoConverter;

import java.util.List;
import matchingGoal.matchingGoal.chat.entity.ChatRoom;
import matchingGoal.matchingGoal.chat.entity.User;
import matchingGoal.matchingGoal.chat.dto.ChatRoomListResponse;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomConverter {

  public List<ChatRoomListResponse> toChatRoomListResponseList(List<ChatRoom> rooms) {


    return rooms.stream().map(this::toChatRoomListResponse).toList();
  }

  public ChatRoomListResponse toChatRoomListResponse(ChatRoom room) {
    List<Long> memberIdList = room.getChatRoomMembers().stream().map(User::getId).toList();

    return ChatRoomListResponse.builder()
        .id(room.getId())
        .members(memberIdList)
        .build();
  }
}
