package matchingGoal.matchingGoal.matching.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.matching.domain.StatusType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponseDto {

  private Long id;
  private Long memberId;
  private String nickname;
//  private Long memberImg;
  private String title;
  private Long img;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;
  private Integer viewCount;
  private StatusType status;
  private Integer requestCount;
  private String region;
  private String stadium;
  private String time;
  private Integer capacity;
  private String content;

}
