package matchingGoal.matchingGoal.dtoConverter;

import java.util.List;
import matchingGoal.matchingGoal.domain.entity.ChatRoom;
import matchingGoal.matchingGoal.domain.entity.User;
import matchingGoal.matchingGoal.dto.ChatRoomListResponse;

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
