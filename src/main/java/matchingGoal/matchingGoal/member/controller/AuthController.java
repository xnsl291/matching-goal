package matchingGoal.matchingGoal.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.member.dto.MemberRegisterDto;
import matchingGoal.matchingGoal.member.dto.SignInDto;
import matchingGoal.matchingGoal.member.dto.GetPasswordDto;
import matchingGoal.matchingGoal.member.dto.SignInResponse;
import matchingGoal.matchingGoal.member.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private static final String AUTH_HEADER = "Authorization";


    /**
     * 회원가입
     * @param registerDto - 회원가입 dto
     * @return "회원가입 성공"
     */
    @PostMapping("/sign-up")
    public ResponseEntity<String> registerMember(@Valid @RequestBody MemberRegisterDto registerDto) {
        return ResponseEntity.ok().body(authService.registerMember(registerDto));
    }

    /**
     * 회원탈퇴
     * @param token - 토큰
     * @param getPasswordDto - 회원 ID, 비밀번호
     * @return "탈퇴 완료"
     */
    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdrawMember(@RequestHeader(name = AUTH_HEADER) String token, @Valid @RequestBody GetPasswordDto getPasswordDto) {
        return ResponseEntity.ok().body(authService.withdrawMember(token, getPasswordDto));
    }

    /**
     * 로그인
     * @param signInDto - 회원 ID, 비밀번호
     * @return SignInResponse - accessToken, refreshToken, id, nickname, imageUrl
     */
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInDto signInDto) {
        return ResponseEntity.ok().body(authService.signIn(signInDto));
    }

    /**
     * 로그아웃
     * @param token - 토큰
     * @return "로그아웃 완료"
     */
    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut(@RequestHeader(name = AUTH_HEADER) String token) {
        return ResponseEntity.ok(authService.signOut(token));
    }
}
