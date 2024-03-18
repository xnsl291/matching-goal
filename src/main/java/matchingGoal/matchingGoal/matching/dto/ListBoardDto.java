package matchingGoal.matchingGoal.matching.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.image.model.entity.Image;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListBoardDto {

  private Long id;
  private Long memberId;
  private String nickname;
  private Long memberImg;
  private String title;
  private LocalDateTime createdDate;
  private Integer viewCount;
  private StatusType status;
  private Integer requestCount;
  private String region;
  private String stadium;
  private LocalDate date;
  private LocalTime time;
}
