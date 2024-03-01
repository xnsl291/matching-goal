package matchingGoal.matchingGoal.member.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.member.dto.MemberRegisterDto;
import matchingGoal.matchingGoal.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;

    /**
     * 회원가입
     * @param registerDto - 회원가입 dto
     * @return "회원가입 성공"
     */
    @Transactional
    @PostMapping("/sign-up")
    public ResponseEntity<?> registerMember(@RequestBody MemberRegisterDto registerDto) {
        memberService.registerMember(registerDto);
        return ResponseEntity.ok().body("회원가입 성공");
    }
}
