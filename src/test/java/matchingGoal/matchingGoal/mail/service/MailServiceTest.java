package matchingGoal.matchingGoal.mail.service;

import matchingGoal.matchingGoal.mail.dto.MailVerificationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailServiceTest {
    @Autowired
    private MailService mailService;

    MailVerificationDto sendMailDto = MailVerificationDto.builder()
            .email("sjfn242@gmail.com")
            .build();


    @Test
    void sendMail(){
        mailService.sendVerificationMail(sendMailDto);
    }

}