package matchingGoal.matchingGoal.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRegisterDto {

    @NotBlank
    private String name;

    @NotBlank()
    @Email
    private String email;

    @NotBlank()
    @Size(min = 10, max = 20, message = "비밀번호는 10자 이상 20자 이하로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{10,}$")
    private String password;

    @NotBlank
    private String nickname;

    private String introduction;

    @NotBlank
    private String region;

    @NotBlank
    private Long imgId;
}
