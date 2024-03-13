package matchingGoal.matchingGoal.mail.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.mail.dto.MailVerificationDto;
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
     * @param email - 이메일
     * @return 발송성공여부
     */
    @GetMapping(value = "/send-verification")
    public ResponseEntity<Boolean> sendVerificationMail(@NotBlank @Email  @RequestParam String email) {
        return ResponseEntity.ok().body(mailService.sendVerificationMail(email));
    }

    /**
     * 인증번호 검증
     * @param mailVerificationDto - email, code, name
     * @return "인증성공"
     */
    @PostMapping(value = "/verify")
    public ResponseEntity<String> verifyMail(@Valid MailVerificationDto mailVerificationDto) {
        mailService.verifyMail(mailVerificationDto);
        return ResponseEntity.ok().body("인증성공");
    }
}
