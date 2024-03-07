package matchingGoal.matchingGoal.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.member.dto.UpdatePwDto;
import matchingGoal.matchingGoal.member.dto.WithdrawMemberDto;
import matchingGoal.matchingGoal.member.exception.*;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.member.dto.MemberRegisterDto;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 비밀번호 포맷 검증
     * @param password - 비밀번호
     * @return 포맷 일치 여부
     */
    public boolean checkPwFormat(String password){
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{10,}$");
    }

    /**
     * 회원가입
     * @param registerDto - 회원가입 dto
     * @return "회원가입 성공"
     */
    @Transactional
    public String registerMember(MemberRegisterDto registerDto) {

        // 이메일 중복 확인
        if(memberRepository.findByEmail(registerDto.getEmail()).isPresent())
            throw new AlreadyRegisteredEmailException(ErrorCode.DUPLICATED_EMAIL);

        // 닉네임 중복 확인
        if (! checkNickname(registerDto.getNickname()))
            throw new DuplicatedNicknameException(ErrorCode.DUPLICATED_NICKNAME);

        // 비밀번호 포맷 검증 : 10자 이상, 알파벳 & 숫자 필수, 특수문자( !@#$%^&*) 입력 가능
        if (! checkPwFormat(registerDto.getPassword()))
            throw new InvalidPasswordFormatException(ErrorCode.INVALID_PASSWORD_FORMAT);

        Member member = Member.builder()
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .nickname(registerDto.getNickname())
                .introduction(registerDto.getIntroduction())
                .region(registerDto.getRegion())
                .imgId(registerDto.getImgId())
                .build();

        memberRepository.save(member);
        return "회원가입 성공";
    }

    /**
     * 회원 탈퇴
     * @param withdrawMemberDto - 회원 ID, PW
     * @return "탈퇴 완료"
     */
    @Transactional
    public String withdrawMember(WithdrawMemberDto withdrawMemberDto) {
        Member member = memberRepository.findById(withdrawMemberDto.getId())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_EXISTS));

        // 탈퇴 전 비밀번호 재확인
        if (!member.getPassword().equals(withdrawMemberDto.getPassword())) {
            throw new UnmatchedPasswordException(ErrorCode.WRONG_PASSWORD);
        }

        member.setDeleted(true);
        member.setDeletedDate(LocalDateTime.now());

        return "탈퇴 완료";
    }

    /**
     * 닉네임 중복 체크
     * @param nickname - 닉네임
     * @return 중복 닉네임 존재시, false 반환
     */
    public Boolean checkNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isEmpty();
    }


    /**
     * 비밀번호 변경
     * @param updatePwDto - 회원 ID, 새로운 PW
     * @return "변경완료"
     */
    public String updatePassword(UpdatePwDto updatePwDto) {
        Member member = memberRepository.findById(updatePwDto.getId()).orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_EXISTS));

        if (! checkPwFormat(updatePwDto.getNewPassword()))
            throw new InvalidPasswordFormatException(ErrorCode.INVALID_PASSWORD_FORMAT);
        member.setPassword(updatePwDto.getNewPassword());
        return "변경완료";
    }
}
