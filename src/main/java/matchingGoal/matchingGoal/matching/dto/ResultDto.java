package matchingGoal.matchingGoal.matching.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultDto {

  @NotNull(message = "상태팀의 점수를 입력해주세요")
  @PositiveOrZero
  private int score1;

  @NotNull(message = "득점한 점수를 입력해주세요")
  @PositiveOrZero
  private int score2;

  @NotNull(message = "경기 시간을 입력해주세요")
  @Positive
  private int duration;

}