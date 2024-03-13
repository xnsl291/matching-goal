package matchingGoal.matchingGoal.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import matchingGoal.matchingGoal.common.annotation.Password;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPasswordDto {

    @NotBlank(message = "비밀번호르 입력해주세요")
    @Password
    private String password;
}
