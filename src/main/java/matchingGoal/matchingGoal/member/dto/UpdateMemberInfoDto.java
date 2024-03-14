package matchingGoal.matchingGoal.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import matchingGoal.matchingGoal.common.annotation.Nickname;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMemberInfoDto {

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Nickname
    private String nickname;

    private String introduction;

    @NotBlank(message = "지역을 입력해주세요")
    private String region;

    @Positive
    private Long imageId;
}
