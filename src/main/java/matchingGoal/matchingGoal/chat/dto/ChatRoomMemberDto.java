package matchingGoal.matchingGoal.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matchingGoal.matchingGoal.member.model.entity.Member;

@Builder
@Getter
@Setter
public class ChatRoomMemberDto {
  private long memberId;
  private String memberNickname;
  private String memberImgUrl;

  public static ChatRoomMemberDto fromEntity(Member entity) {
    return ChatRoomMemberDto.builder()
        .memberId(entity.getId())
        .memberNickname(entity.getNickname())
        .memberImgUrl(entity.getImageUrl())
        .build();
  }
}
