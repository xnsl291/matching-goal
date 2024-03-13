package matchingGoal.matchingGoal.member.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.common.annotation.Nickname;
import matchingGoal.matchingGoal.member.dto.GetPasswordDto;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private static final String AUTH_HEADER = "Authorization";
    private final MemberService memberService;

    /**
     * 중복된 닉네임이 존재하는지 확인
     * @param nickname - 닉네임
     * @return 중복여부 (중복 시, false)
     */
    @PostMapping("/checkNickname")
    public ResponseEntity<Boolean> isDuplicatedNickname(@NotBlank @Nickname @RequestParam String nickname) {
        return ResponseEntity.ok().body(memberService.isDuplicatedNickname(nickname));
    }

    /**
     * 비밀번호 변경
     * @param token - 토큰
     * @param dto - 새로운 비밀번호
     * @return "변경완료"
     */
    @PatchMapping("/members/password")
    public ResponseEntity<String> updatePassword(@RequestHeader(name = AUTH_HEADER) String token, @Valid @RequestBody GetPasswordDto dto) {
        return ResponseEntity.ok().body(memberService.updatePassword(token, dto));
    }

    /**
     * 개인 정보 조회
     * @param token - 토큰
     * @return Member
     */
    @GetMapping("/")
    public ResponseEntity<Member> getMemberInfo(@RequestHeader(name = AUTH_HEADER) String token) {
        return ResponseEntity.ok().body(memberService.getMemberInfo(token));
    }
}
