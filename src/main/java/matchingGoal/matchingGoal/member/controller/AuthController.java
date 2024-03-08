package matchingGoal.matchingGoal.member.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.common.annotation.Nickname;
import matchingGoal.matchingGoal.mail.service.MailService;
import matchingGoal.matchingGoal.member.dto.MemberRegisterDto;
import matchingGoal.matchingGoal.mail.dto.MailVerificationDto;
import matchingGoal.matchingGoal.member.dto.UpdatePwDto;
import matchingGoal.matchingGoal.member.dto.WithdrawMemberDto;
import matchingGoal.matchingGoal.member.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final MailService mailService;
    private final AuthService authService;

    /**
     * 회원가입
     * @param registerDto - 회원가입 dto
     * @return "회원가입 성공"
     */
    @Transactional
    @PostMapping("/sign-up")
    public ResponseEntity<String> registerMember(@Valid @RequestBody MemberRegisterDto registerDto) {
        return ResponseEntity.ok().body(authService.registerMember(registerDto));
    }

    /**
     * 회원탈퇴
     * @param withdrawMemberDto - 회원 ID, PW
     * @return "탈퇴 완료"
     */
    @DeleteMapping("/withdraw")
    //TODO: memberId는 token에서 얻어오는 방식으로 변경
    public ResponseEntity<String> withdrawMember(@Valid @RequestBody WithdrawMemberDto withdrawMemberDto) {
        return ResponseEntity.ok().body(authService.withdrawMember(withdrawMemberDto));
    }

    /**
     * 중복된 닉네임이 존재하는지 확인
     * @param nickname - 닉네임
     * @return 중복여부 (중복 시, false)
     */
    @PostMapping("/checkNickname")
    public ResponseEntity<Boolean> checkNickname(@NotBlank @Nickname @RequestParam String nickname) {
        return ResponseEntity.ok().body(authService.checkNickname(nickname));
    }

    /**
     * 비밀번호 변경
     * @param updatePwDto - 회원 ID, 새로운 PW
     * @return "변경완료"
     */
    @PatchMapping("/password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody UpdatePwDto updatePwDto) {
        return ResponseEntity.ok().body(authService.updatePassword(updatePwDto));
    }

    /**
     * 인증메일 발송
     * @param email - 이메일
     * @return 발송성공여부
     */
    @GetMapping(value = "/mails/send-verification")
    public ResponseEntity<Boolean> verifyMail(@NotBlank @Email  @RequestParam String email) {
        return ResponseEntity.ok().body(mailService.sendVerificationMail(email));
    }

    /**
     * 인증번호 검증
     * @param mailVerificationDto - email, code, name
     * @return "인증성공"
     */
    @PostMapping(value = "/mails/verify")
    public ResponseEntity<String> verifyMail(@Valid MailVerificationDto mailVerificationDto) {
        mailService.verifyMail(mailVerificationDto);
        return ResponseEntity.ok().body("인증성공");
    }
}
