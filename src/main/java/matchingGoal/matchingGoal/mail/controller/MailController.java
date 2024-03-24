package matchingGoal.matchingGoal.mail.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.mail.dto.MailVerificationDto;
import matchingGoal.matchingGoal.mail.dto.SendMailVerificationDto;
import matchingGoal.matchingGoal.mail.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mails")
public class MailController {

    private final MailService mailService;

    /**
     * 인증메일 발송
     * @return 발송성공여부
     */
    @PostMapping("/send-verification")
    public ResponseEntity<Boolean> sendVerificationMail(@Valid @RequestBody SendMailVerificationDto sendMailVerificationDto) {
        return ResponseEntity.ok().body(mailService.sendVerificationMail(sendMailVerificationDto));
    }

    /**
     * 인증번호 검증
     * @param mailVerificationDto - email, code, name
     * @return "인증성공"
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyMail(@Valid @RequestBody MailVerificationDto mailVerificationDto) {
        mailService.verifyMail(mailVerificationDto);
        return ResponseEntity.ok().body("인증성공");
    }
}
