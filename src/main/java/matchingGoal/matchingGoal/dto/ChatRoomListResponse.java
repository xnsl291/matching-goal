package matchingGoal.matchingGoal.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;

@Builder
public class ChatRoomListResponse {
  private String id;
  @Default
  private List<Long> members = new ArrayList<>();

}
