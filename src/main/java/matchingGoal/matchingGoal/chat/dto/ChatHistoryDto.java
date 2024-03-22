package matchingGoal.matchingGoal.chat.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ChatHistoryDto {

  private List<ChatRoomMemberDto> chatRoomMemberInfo;
  private List<ChatMessageDto> chatMessageList;

}
