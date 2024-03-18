package matchingGoal.matchingGoal.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import matchingGoal.matchingGoal.common.annotation.Password;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePasswordDto {
    @NotBlank(message = "기존 비밀번호를 입력해주세요")
    @Password
    private String oldPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요")
    @Password
    private String newPassword;
}
