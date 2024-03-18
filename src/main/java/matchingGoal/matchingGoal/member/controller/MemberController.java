package matchingGoal.matchingGoal.member.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.common.annotation.Nickname;
import matchingGoal.matchingGoal.member.dto.SimplerInfoResponse;
import matchingGoal.matchingGoal.member.dto.UpdateMemberInfoDto;
import matchingGoal.matchingGoal.member.dto.UpdatePasswordDto;
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
    @PatchMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestHeader(name = AUTH_HEADER) String token, @Valid @RequestBody UpdatePasswordDto dto) {
        return ResponseEntity.ok().body(memberService.updatePassword(token, dto));
    }

    /**
     * 개인 정보 조회
     * @param token - 토큰
     * @return Member
     */
    @GetMapping("/mypage")
    public ResponseEntity<Member> getMemberInfo(@RequestHeader(name = AUTH_HEADER) String token) {
        return ResponseEntity.ok().body(memberService.getMemberInfo(token));
    }
    /**
     * 개인 정보 수정
     * @param token - 토큰
     * @param updateDto - 정보 수정 dto (이름, 닉네임, 소개, 지역, 이미지)
     * @return "수정완료"
     */
    @PatchMapping("/mypage")
    public ResponseEntity<String> editMemberInfo(@RequestHeader(name = AUTH_HEADER) String token, @Valid UpdateMemberInfoDto updateDto) {
        return ResponseEntity.ok().body(memberService.editMemberInfo(token,updateDto));
    }

    /**
     * 다른 회원 정보 조회
     * @param memberId - 조회하고 싶은 회원 ID
     * @return OtherMemberInfoResponse - 닉네임, 소개, 지역, 이미지url
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<SimplerInfoResponse> getSimpleUserinfo(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok().body(memberService.getSimpleUserinfo(memberId));
    }

}
