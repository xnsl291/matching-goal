package matchingGoal.matchingGoal.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInResponse {
    private String accessToken;

    private String refreshToken;

    private Long memberId;

    private String nickname;

    private String imageUrl;
}
