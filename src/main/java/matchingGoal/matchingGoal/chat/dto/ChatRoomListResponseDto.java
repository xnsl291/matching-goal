package matchingGoal.matchingGoal.chat.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matchingGoal.matchingGoal.chat.entity.ChatRoom;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ChatRoomListResponseDto {

  private String id;
  private long matchingBoardId;
  private List<ChatRoomMemberDto> memberList;

  public static ChatRoomListResponseDto fromEntity(ChatRoom entity) {

    return ChatRoomListResponseDto.builder()
        .id(entity.getId())
        .matchingBoardId(entity.getMatchingBoardId())
        .memberList(
            entity.getChatRoomMembers().stream().map(ChatRoomMemberDto::fromEntity).toList())
        .build();
  }
}
