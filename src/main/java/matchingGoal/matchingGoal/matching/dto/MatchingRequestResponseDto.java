package matchingGoal.matchingGoal.matching.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingRequest;
import matchingGoal.matchingGoal.member.model.entity.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingRequestResponseDto {

  private Long id;
  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime createdDate;
  private Long memberId;
  private String nickname;
  private String memberImg;

  public static MatchingRequestResponseDto of(MatchingRequest matchingRequest) {
    Member member = matchingRequest.getMember();
    return MatchingRequestResponseDto.builder()
        .id(matchingRequest.getId())
        .createdDate(matchingRequest.getCreatedDate())
        .memberId(member.getId())
        .nickname(member.getNickname())
        .memberImg(member.getImageUrl())
        .build();
  }
}
