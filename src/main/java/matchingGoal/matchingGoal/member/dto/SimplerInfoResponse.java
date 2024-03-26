package matchingGoal.matchingGoal.member.dto;

import lombok.*;
import matchingGoal.matchingGoal.member.model.entity.Member;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimplerInfoResponse {

    private String nickname;

    private String introduction;

    private String region;

    private String imageUrl;

    public static SimplerInfoResponse of(Member member) {
        return SimplerInfoResponse.builder()
                .nickname(member.getNickname())
                .introduction(member.getIntroduction())
                .region(member.getRegion())
                .imageUrl(member.getImageUrl())
                .build();
    }

}
