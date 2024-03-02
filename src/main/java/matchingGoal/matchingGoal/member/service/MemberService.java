package matchingGoal.matchingGoal.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.exception.AlreadyRegisteredEmailException;
import matchingGoal.matchingGoal.common.exception.InvalidPasswordFormatException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.member.dto.MemberRegisterDto;
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

    public void registerMember(MemberRegisterDto registerDto) {

        // 이메일 중복 확인
        memberRepository.findByEmail(registerDto.getEmail())
                .ifPresent(u -> {
                    throw new AlreadyRegisteredEmailException(ErrorCode.ALREADY_REGISTERED_EMAIL);
                });

        // 비밀번호 포맷 검증 : 10자 이상, 알파벳 & 숫자 필수, 특수문자( !@#$%^&*) 입력 가능
        if (!registerDto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{10,}$"))
        {
            throw new InvalidPasswordFormatException(ErrorCode.INVALID_PASSWORD_FORMAT);
        }

        Member member = Member.builder()
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .password(registerDto.getPassword())
                .nickname(registerDto.getNickname())
                .introduction(registerDto.getIntroduction())
                .region(registerDto.getRegion())
                .build();

        memberRepository.save(member);
    }
}
