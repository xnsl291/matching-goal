package matchingGoal.matchingGoal.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.member.dto.GetPasswordDto;
import matchingGoal.matchingGoal.member.exception.InvalidTokenException;
import matchingGoal.matchingGoal.member.exception.MemberNotFoundException;
import matchingGoal.matchingGoal.member.exception.WithdrawnMemberAccessException;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

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
     * @param token - 토큰
     * @param getPasswordDto - 비밀번호
     * @return "변경완료"
     */
    @Transactional
    public String updatePassword(String token, GetPasswordDto getPasswordDto) {
        Member member = getMemberByToken(token);
        member.setPassword(getPasswordDto.getPassword());
        return "변경완료";
    }

    /**
     * 토큰을 사용하여 회원 검색
     * @param token - 토큰
     * @return Member
     */
    public Member getMemberByToken(String token){
        Long memberId = jwtTokenProvider.getId(token);
        return  memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    /**
     * 개인 정보 조회
     * @param token - 토큰
     * @return Member
     */
    public Member getMemberInfo(String token) {
        if(!jwtTokenProvider.validateToken(token))
            throw new InvalidTokenException();

        String email = jwtTokenProvider.getEmail(token);
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        if (member.isDeleted())
            throw new WithdrawnMemberAccessException();

        return member;
    }

}
