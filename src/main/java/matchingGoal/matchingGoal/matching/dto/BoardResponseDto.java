package matchingGoal.matchingGoal.matching.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponseDto {

  private long id;
  private long memberId;
  private String nickname;
  private String memberImg;
  private String title;
  private String content;
  private List<String> imageUrls;
  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime createdDate;
  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime modifiedDate;
  private int viewCount;
  private StatusType status;
  private Integer requestCount;
  private String region;
  private String stadium;
  private LocalDate date;
  @JsonFormat(shape = Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
  private LocalTime time;

  public static BoardResponseDto of(MatchingBoard matchingBoard) {
    return BoardResponseDto.builder()
        .id(matchingBoard.getId())
        .memberId(matchingBoard.getMember().getId())
        .memberImg(matchingBoard.getMember().getImageUrl())
        .nickname(matchingBoard.getMember().getNickname())
        .title(matchingBoard.getTitle())
        .content(matchingBoard.getContent())
        .imageUrls(matchingBoard.getImageUrls())
        .createdDate(matchingBoard.getCreatedDate())
        .modifiedDate(matchingBoard.getModifiedDate())
        .viewCount(matchingBoard.getViewCount())
        .status(matchingBoard.getStatus())
        .requestCount(matchingBoard.getMember().getRequestCount())
        .region(matchingBoard.getRegion())
        .stadium(matchingBoard.getGame().getStadiumName())
        .date(matchingBoard.getGame().getDate())
        .time(matchingBoard.getGame().getTime())
        .build();
  }

}
