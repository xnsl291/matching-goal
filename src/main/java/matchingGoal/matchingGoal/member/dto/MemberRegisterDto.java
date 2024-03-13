package matchingGoal.matchingGoal.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import matchingGoal.matchingGoal.common.annotation.Nickname;
import matchingGoal.matchingGoal.common.annotation.Password;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRegisterDto {

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @Email
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Password
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Nickname
    private String nickname;

    private String introduction;

    @NotBlank(message = "지역을 입력해주세요")
    private String region;

    @Positive
    private Long imageId;
}
