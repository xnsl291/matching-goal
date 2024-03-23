package matchingGoal.matchingGoal.member.service;

import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.auth.JwtToken;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import matchingGoal.matchingGoal.member.dto.SignInDto;
import matchingGoal.matchingGoal.member.dto.GetPasswordDto;
import matchingGoal.matchingGoal.member.dto.SignInResponse;
import matchingGoal.matchingGoal.member.dto.SignUpDto;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * 회원가입
     * @param registerDto - 회원가입 dto
     * @return "회원가입 성공"
     */
    public String registerMember(SignUpDto registerDto) {

        // 이메일 중복 확인
        if(memberRepository.findByEmail(registerDto.getEmail()).isPresent())
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_EMAIL);

        // 닉네임 중복 확인
        if (! memberService.isDuplicatedNickname(registerDto.getNickname()))
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);

        Member member = Member.builder()
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .nickname(registerDto.getNickname())
                .introduction(registerDto.getIntroduction())
                .region(registerDto.getRegion())
                .build();

        memberRepository.save(member);
        return "회원가입 성공";
    }

    /**
     * 회원 탈퇴
     * @param getPasswordDto - 비밀번호
     * @return "탈퇴 완료"
     */
    @Transactional
    public String withdrawMember(String token, GetPasswordDto getPasswordDto) {
        Member member = memberService.getMemberInfo(token);

        // 탈퇴 전 비밀번호 재확인
        memberService.isMatchedPassword(getPasswordDto.getPassword(), member.getPassword());

        member.setDeleted(true);
        member.setDeletedDate(LocalDateTime.now());

        return "탈퇴 완료";
    }

    /**
     * 로그인
     * @param signInDto - 회원 ID, 비밀번호
     * @return SignInResponse - accessToken, refreshToken, id, nickname, email, imageUrl
     */
    public SignInResponse signIn(SignInDto signInDto) {
        Member member = memberRepository.findByEmail(signInDto.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 비밀번호 체크
        memberService.isMatchedPassword(signInDto.getPassword(),member.getPassword());

        if(member.isDeleted())
            throw new CustomException(ErrorCode.WITHDRAWN_MEMBER);

        // 토큰 발행
        JwtToken tokens = jwtTokenProvider.generateToken(member.getId(), member.getEmail(), member.getNickname());

        return SignInResponse.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .memberId(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .build();
    }

    /**
     * 로그아웃
     * @return "로그아웃 완료"
     */
    public String signOut(String token) {
        jwtTokenProvider.validateToken(token);

        // 블랙 리스트에 추가(로그아웃)
        jwtTokenProvider.setBlacklist(token);

        return "로그아웃 완료";
    }
}
