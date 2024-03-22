package matchingGoal.matchingGoal.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.matching.domain.entity.Result;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultResponse {

  private Long resultId;
  private Long gameId;
  private Integer score1;
  private Integer score2;
  private Integer duration;

  public static ResultResponse of(Result result) {
    return ResultResponse.builder()
        .resultId(result.getId())
        .gameId(result.getGame().getId())
        .score1(result.getScore1())
        .score2(result.getScore2())
        .duration(result.getDuration())
        .build();
  }

}
