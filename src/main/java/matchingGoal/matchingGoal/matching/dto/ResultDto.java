package matchingGoal.matchingGoal.matching.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultDto {

  @NotNull(message = "승리팀을 선택해주세요")
  private Long winnerId;

  @NotNull(message = "점수를 모두 입력해주세요")
  private Integer score1;

  @NotNull(message = "점수를 모두 입력해주세요")
  private Integer score2;

  @NotNull(message = "경기 시간을 입력해주세요")
  private Integer duration;

}