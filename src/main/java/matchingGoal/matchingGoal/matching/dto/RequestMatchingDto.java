package matchingGoal.matchingGoal.matching.dto;

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
public class RequestMatchingDto {

  private Long id;
  private LocalDateTime createdDate;
  private Long memberId;
  private String nickname;
  private String memberImg;

  public static RequestMatchingDto of(MatchingRequest matchingRequest) {
    Member member = matchingRequest.getMember();
    return RequestMatchingDto.builder()
        .id(matchingRequest.getId())
        .createdDate(matchingRequest.getCreatedDate())
        .memberId(member.getId())
        .nickname(member.getNickname())
        .memberImg(member.getImageUrl())
        .build();
  }
}
