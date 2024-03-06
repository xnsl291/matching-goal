package matchingGoal.matchingGoal.mail.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MailVerificationDto {
  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String code;

  @NotBlank
  private String name;

}
