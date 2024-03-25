package matchingGoal.matchingGoal.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import matchingGoal.matchingGoal.common.annotation.Nickname;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMemberDto {

    private String name;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Nickname
    private String nickname;

    private String introduction;

    private String region;

    private String imageUrl;
}
