package matchingGoal.matchingGoal.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.auth.JwtToken;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import matchingGoal.matchingGoal.common.service.RedisService;
import matchingGoal.matchingGoal.member.dto.SignInDto;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final String TOKEN_PREFIX = "RT_";

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
     * @param withdrawMemberDto - 회원 ID, Password
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
     * 로그인
     * @param signInDto - 회원 ID, 비밀번호
     * @return token - 토큰
     */
    @Transactional
    public JwtToken signIn(SignInDto signInDto) {
        Member member = memberRepository.findByEmail(signInDto.getEmail()).orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_EXISTS));

        // 비밀번호 체크
        boolean isMatch = passwordEncoder.matches(signInDto.getPassword(),member.getPassword());
        log.info("\n\n"+member.getPassword()   +"    " + signInDto.getPassword()  +" " +passwordEncoder.matches(member.getPassword() ,signInDto.getPassword())  );
        if (!isMatch) {
            throw new InvalidPasswordException(ErrorCode.WRONG_PASSWORD);
        }

        if(member.isDeleted())
            throw new WithdrawnMemberAccessException(ErrorCode.WITHDRAWN_MEMBER);

        // 토큰 발행
        return jwtTokenProvider.generateToken(member.getId(), member.getEmail());
    }

    /**
     * 로그아웃
     * @param token - 토큰
     * @return "로그아웃 완료"
     */
    @Transactional
    public String signOut(String token) {

        if(!jwtTokenProvider.validateToken(token))
            throw new InvalidTokenException(ErrorCode.EXPIRED_TOKEN);

        String email = jwtTokenProvider.getEmail(token);
        if (redisService.getData(TOKEN_PREFIX + email) == null) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }

        // 블랙 리스트에 추가(로그아웃)
        jwtTokenProvider.setBlacklist(email);

        return "로그아웃 완료";
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
