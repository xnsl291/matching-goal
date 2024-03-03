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
}
