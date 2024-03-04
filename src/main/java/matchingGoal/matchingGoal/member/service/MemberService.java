package matchingGoal.matchingGoal.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.member.exception.AlreadyRegisteredEmailException;
import matchingGoal.matchingGoal.member.exception.DuplicatedNicknameException;
import matchingGoal.matchingGoal.member.exception.InvalidPasswordFormatException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.member.dto.MemberRegisterDto;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * @param registerDto - 회원가입 dto
     */
    public void registerMember(MemberRegisterDto registerDto) {

        // 이메일 중복 확인
        if(memberRepository.findByEmail(registerDto.getEmail()).isPresent())
            throw new AlreadyRegisteredEmailException(ErrorCode.DUPLICATED_EMAIL);

        // 닉네임 중복 확인
        if (! checkNickname(registerDto.getNickname()))
            throw new DuplicatedNicknameException(ErrorCode.DUPLICATED_NICKNAME);

        // 비밀번호 포맷 검증 : 10자 이상, 알파벳 & 숫자 필수, 특수문자( !@#$%^&*) 입력 가능
        if (!registerDto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{10,}$"))
            throw new InvalidPasswordFormatException(ErrorCode.INVALID_PASSWORD_FORMAT);

        Member member = Member.builder()
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .nickname(registerDto.getNickname())
                .introduction(registerDto.getIntroduction())
                .region(registerDto.getRegion())
                .build();

        memberRepository.save(member);
    }

    /**
     * 닉네임 중복 체크
     * @param nickname - 닉네임
     * @return 중복 닉네임 존재시, false 반환
     */
    public Boolean checkNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isEmpty();
    }
}
