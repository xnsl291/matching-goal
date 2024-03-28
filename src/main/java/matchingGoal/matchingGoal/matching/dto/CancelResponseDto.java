package matchingGoal.matchingGoal.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.matching.domain.entity.GameCancel;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelResponseDto {

  private long cancelId;
  private long memberId;
  private long gameId;

  public static CancelResponseDto of(GameCancel cancel) {
    return CancelResponseDto.builder()
        .cancelId(cancel.getId())
        .memberId(cancel.getMember().getId())
        .gameId(cancel.getGame().getId())
        .build();
  }
}