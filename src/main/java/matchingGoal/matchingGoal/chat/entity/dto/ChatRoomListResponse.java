package matchingGoal.matchingGoal.chat.entity.dto;

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
  private List<Long> memberIdList;
  private List<String> memberNicknameList;

  public static ChatRoomListResponse fromEntity(ChatRoom entity) {
    List<Long> memberIdList = entity.getChatRoomMembers().stream().map(Member::getId).toList();
    List<String> memberNicknameList = entity.getChatRoomMembers().stream().map(Member::getNickname).toList();
    return ChatRoomListResponse.builder()
        .id(entity.getId())
        .memberIdList(memberIdList)
        .memberNicknameList(memberNicknameList)
        .build();
  }
}
