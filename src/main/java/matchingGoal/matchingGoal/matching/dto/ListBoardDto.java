package matchingGoal.matchingGoal.matching.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.matching.domain.StatusType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListBoardDto {

  private long id;
  private long memberId;
  private String nickname;
  private String memberImgUrl;
  private String title;
  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime createdDate;
  private int viewCount;
  private StatusType status;
  private int requestCount;
  private String region;
  private String stadium;
  private LocalDate date;
  @JsonFormat(shape = Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
  private LocalTime time;
}
