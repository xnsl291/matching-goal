package matchingGoal.matchingGoal.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.auth.JwtToken;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import matchingGoal.matchingGoal.common.service.RedisService;
import matchingGoal.matchingGoal.image.service.ImageService;
import matchingGoal.matchingGoal.member.dto.SignInDto;
import matchingGoal.matchingGoal.member.dto.GetPasswordDto;
import matchingGoal.matchingGoal.member.dto.SignInResponse;
import matchingGoal.matchingGoal.member.exception.*;
import matchingGoal.matchingGoal.member.dto.MemberRegisterDto;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ImageService imageService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final RedisService redisService;
    private final String TOKEN_PREFIX = "RT_";

    /**
     * 회원가입
     * @param registerDto - 회원가입 dto
     * @return "회원가입 성공"
     */
    public String registerMember(MemberRegisterDto registerDto) {

        // 이메일 중복 확인
        if(memberRepository.findByEmail(registerDto.getEmail()).isPresent())
            throw new AlreadyRegisteredEmailException();

        // 닉네임 중복 확인
        if (! memberService.isDuplicatedNickname(registerDto.getNickname()))
            throw new DuplicatedNicknameException();

        Member member = Member.builder()
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .nickname(registerDto.getNickname())
                .introduction(registerDto.getIntroduction())
                .region(registerDto.getRegion())
                .imageId(registerDto.getImageId())
                .build();

        memberRepository.save(member);
        return "회원가입 성공";
    }

    /**
     * 회원 탈퇴
     * @param token - 토큰
     * @param getPasswordDto - 비밀번호
     * @return "탈퇴 완료"
     */
    public String withdrawMember(String token, GetPasswordDto getPasswordDto) {
        Member member = memberService.getMemberByToken(token);

        // 탈퇴 전 비밀번호 재확인
        if (!member.getPassword().equals(getPasswordDto.getPassword())) {
            throw new UnmatchedPasswordException();
        }

        member.setDeleted(true);
        member.setDeletedDate(LocalDateTime.now());

        return "탈퇴 완료";
    }

    /**
     * 로그인
     * @param signInDto - 회원 ID, 비밀번호
     * @return SignInResponse - accessToken, refreshToken, id, nickname, imageUrl
     */
    public SignInResponse signIn(SignInDto signInDto) {
        Member member = memberRepository.findByEmail(signInDto.getEmail()).orElseThrow(MemberNotFoundException::new);

        // 비밀번호 체크
        boolean isMatch = passwordEncoder.matches(signInDto.getPassword(),member.getPassword());
        if (!isMatch) {
            throw new UnmatchedPasswordException();
        }

        if(member.isDeleted())
            throw new WithdrawnMemberAccessException();

        // 토큰 발행
        JwtToken tokens = jwtTokenProvider.generateToken(member.getId(), member.getEmail());

        //프로필이미지
        String imageUrl = imageService.getImageUrl(member.getImageId());

        return SignInResponse.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .id(member.getId())
                .nickname(member.getNickname())
                .imageUrl(imageUrl).build();
    }

    /**
     * 로그아웃
     * @param token - 토큰
     * @return "로그아웃 완료"
     */
    public String signOut(String token) {

        if(!jwtTokenProvider.validateToken(token))
            throw new InvalidTokenException();

        String email = jwtTokenProvider.getEmail(token);
        if (redisService.getData(TOKEN_PREFIX + email) == null) {
            throw new InvalidTokenException();
        }

        // 블랙 리스트에 추가(로그아웃)
        jwtTokenProvider.setBlacklist(email);

        return "로그아웃 완료";
    }

}
