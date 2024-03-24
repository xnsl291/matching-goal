package matchingGoal.matchingGoal.mail.service;

import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.service.RedisService;
import matchingGoal.matchingGoal.mail.dto.MailVerificationDto;
import matchingGoal.matchingGoal.mail.dto.SendMailVerificationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisService redisService;

    @Value("${spring.mail.username}")
    private String username;

    SendMailVerificationDto dto = SendMailVerificationDto.builder()
            .email(username).build();
    @Test
    void sendMail(){
        mailService.sendVerificationMail(dto);
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

        assertThrows(CustomException.class, () -> mailService.verifyMail(mailVerificationDto));
    }

    @Test
    public void testVerifyMailWithValidCode() {
        String email = username;
        String code = redisService.getData("VAL_"+email);
        String name = "anonymous";

        MailVerificationDto mailVerificationDto = MailVerificationDto.builder()
                .code(code)
                .name(name)
                .email(email)
                .build();

        mailService.verifyMail(mailVerificationDto);
    }

}