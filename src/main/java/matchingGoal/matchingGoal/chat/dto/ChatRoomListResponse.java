package matchingGoal.matchingGoal.chat.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matchingGoal.matchingGoal.chat.entity.ChatRoom;
import matchingGoal.matchingGoal.member.model.entity.Member;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ChatRoomListResponse {
  private String id;
  private List<ChatRoomMemberDto> memberList;
  public static ChatRoomListResponse fromEntity(ChatRoom entity) {
    return ChatRoomListResponse.builder()
        .id(entity.getId())
        .memberList(entity.getChatRoomMembers().stream().map(ChatRoomMemberDto::fromEntity).toList())
        .build();
  }
}
