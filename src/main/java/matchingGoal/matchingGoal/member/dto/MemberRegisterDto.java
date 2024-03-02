package matchingGoal.matchingGoal.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRegisterDto {
    private String name;
    private String email;
    private String password;
    private String nickname;
    private String introduction;
    private String region;
}
