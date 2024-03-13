package matchingGoal.matchingGoal.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtherMemberInfoResponse {

    private String nickname;

    private String introduction;

    private String region;

    private String imageUrl;
}
