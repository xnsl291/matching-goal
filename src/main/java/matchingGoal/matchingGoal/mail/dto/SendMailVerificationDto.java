package matchingGoal.matchingGoal.mail.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendMailVerificationDto {
  @Email
  @NotBlank(message = "이메일을 입력해주세요")
  private String email;
}
