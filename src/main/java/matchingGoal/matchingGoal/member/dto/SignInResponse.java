package matchingGoal.matchingGoal.member.dto;

import lombok.*;
import matchingGoal.matchingGoal.common.auth.JwtToken;
import matchingGoal.matchingGoal.member.model.entity.Member;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInResponse {
    private String accessToken;

    private String refreshToken;

    private Long memberId;

    private String email;

    private String nickname;

    private String imageUrl;

    public static SignInResponse of(JwtToken token, Member member) {
        return SignInResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .imageUrl(member.getImageUrl())
                .build();
    }
}
