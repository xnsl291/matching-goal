package matchingGoal.matchingGoal.mail.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MailDto {
    private String from;
    private String to;
    private String subject;
    private String template;
}
