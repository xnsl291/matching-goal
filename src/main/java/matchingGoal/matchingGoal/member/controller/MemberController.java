package matchingGoal.matchingGoal.member.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.common.annotation.Nickname;
import matchingGoal.matchingGoal.member.dto.UpdatePwDto;
import matchingGoal.matchingGoal.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    /**
     * 중복된 닉네임이 존재하는지 확인
     * @param nickname - 닉네임
     * @return 중복여부 (중복 시, false)
     */
    @PostMapping("members/checkNickname")
    public ResponseEntity<Boolean> isDuplicatedNickname(@NotBlank @Nickname @RequestParam String nickname) {
        return ResponseEntity.ok().body(memberService.isDuplicatedNickname(nickname));
    }

    /**
     * 비밀번호 변경
     * @param updatePwDto - 회원 ID, 새로운 Password
     * @return "변경완료"
     */
    @PatchMapping("members/password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePwDto updatePwDto) {
        return ResponseEntity.ok().body(memberService.updatePassword(updatePwDto));
    }
}
