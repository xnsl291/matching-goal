package matchingGoal.matchingGoal.member.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.mail.service.MailService;
import matchingGoal.matchingGoal.member.dto.MemberRegisterDto;
import matchingGoal.matchingGoal.mail.dto.MailVerificationDto;
import matchingGoal.matchingGoal.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;
    private final MailService mailService;

    /**
     * 회원가입
     * @param registerDto - 회원가입 dto
     * @return "회원가입 성공"
     */
    @Transactional
    @PostMapping("/sign-up")
    public ResponseEntity<String> registerMember(@RequestBody MemberRegisterDto registerDto) {
        memberService.registerMember(registerDto);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    /**
     * 중복된 닉네임이 존재하는지 확인
     * @param nickname - 닉네임
     * @return 중복여부 (중복 시, false)
     */
    @PostMapping("/checkNickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        return ResponseEntity.ok().body(memberService.checkNickname(nickname));
    }

    /**
     * 인증메일 발송
     * @param email - 이메일
     * @return 발송성공여부
     */
    @GetMapping(value = "/mails/send-verification")
    public ResponseEntity<Boolean> verifyMail(@RequestParam String email) {
        return ResponseEntity.ok().body(mailService.sendVerificationMail(email));
    }

    /**
     * 인증번호 검증
     * @param mailVerificationDto - email, code, name
     * @return "인증성공"
     */
    @PostMapping(value = "/mails/verify")
    public ResponseEntity<String> verifyMail(
            MailVerificationDto mailVerificationDto) {
        mailService.verifyMail(mailVerificationDto);
        return ResponseEntity.ok().body("인증성공");
    }
}
