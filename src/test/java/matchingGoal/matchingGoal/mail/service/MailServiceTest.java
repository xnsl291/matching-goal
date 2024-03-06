package matchingGoal.matchingGoal.mail.service;

import matchingGoal.matchingGoal.mail.dto.MailVerificationDto;
import matchingGoal.matchingGoal.mail.exception.InvalidValidationCodeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Value("${spring.mail.username}")
    private String username;

    @Test
    void sendMail(){
        mailService.sendVerificationMail(username);
    }

    @Test
    public void testVerifyMailWithInvalidCode() {
        String email = username;
        String code = "invalid_code";
        String name = "anonymous";

        MailVerificationDto mailVerificationDto = MailVerificationDto.builder()
                .code(code)
                .name(name)
                .email(email)
                .build();

        assertThrows(InvalidValidationCodeException.class, () -> mailService.verifyMail(mailVerificationDto));
    }

    @Test
    public void testVerifyMailWithValidCode() {
        String email = username;
        String code = "EaMB1nR";
        String name = "anonymous";

        MailVerificationDto mailVerificationDto = MailVerificationDto.builder()
                .code(code)
                .name(name)
                .email(email)
                .build();

        mailService.verifyMail(mailVerificationDto);
    }

}