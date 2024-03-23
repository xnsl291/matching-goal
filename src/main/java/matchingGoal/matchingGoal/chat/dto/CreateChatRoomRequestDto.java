package matchingGoal.matchingGoal.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatRoomRequestDto {

  private long guestId;
  private long matchingBoardId;

}
