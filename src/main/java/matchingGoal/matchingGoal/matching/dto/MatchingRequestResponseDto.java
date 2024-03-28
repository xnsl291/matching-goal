package matchingGoal.matchingGoal.matching.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingRequest;
import matchingGoal.matchingGoal.member.model.entity.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingRequestResponseDto {

  private long id;
  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime createdDate;
  private long memberId;
  private String nickname;
  private String memberImg;
  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime gameDate;
  private String stadiumName;


  public static MatchingRequestResponseDto of(MatchingRequest matchingRequest) {
    Member member = matchingRequest.getMember();
    Game game = matchingRequest.getBoard().getGame();
    return MatchingRequestResponseDto.builder()
        .id(matchingRequest.getId())
        .createdDate(matchingRequest.getCreatedDate())
        .memberId(member.getId())
        .nickname(member.getNickname())
        .memberImg(member.getImageUrl())
        .gameDate(LocalDateTime.of(game.getDate(), game.getTime()))
        .stadiumName(game.getStadiumName())
        .build();
  }
}
