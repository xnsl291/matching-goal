package matchingGoal.matchingGoal.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.member.dto.UpdatePwDto;
import matchingGoal.matchingGoal.member.exception.*;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 닉네임 중복 체크
     * @param nickname - 닉네임
     * @return 중복 닉네임 존재시, false 반환
     */
    public Boolean isDuplicatedNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isEmpty();
    }

    /**
     * 비밀번호 변경
     * @param updatePwDto - 회원 ID, 새로운 Password
     * @return "변경완료"
     */
    public String updatePassword(UpdatePwDto updatePwDto) {
        Member member = memberRepository.findById(updatePwDto.getId())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_EXISTS));

        member.setPassword(updatePwDto.getNewPassword());
        return "변경완료";
    }

}
