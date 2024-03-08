package matchingGoal.matchingGoal.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequestDto {

  private Long memberId;
//  private Long imgId;
  private String region;
  private String stadium;
  private String date;
  private String time;
  private String title;
  private String content;

}
