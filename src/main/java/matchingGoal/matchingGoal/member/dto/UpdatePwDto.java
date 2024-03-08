package matchingGoal.matchingGoal.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import matchingGoal.matchingGoal.common.annotation.Password;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePwDto {

    @NotBlank
    @Positive
    private Long id;

    @NotBlank(message = "비밀번호르 입력해주세요")
    @Password
    private String newPassword;
}
