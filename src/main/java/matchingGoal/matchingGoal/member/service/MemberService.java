package matchingGoal.matchingGoal.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.matching.domain.CancelType;
import matchingGoal.matchingGoal.matching.domain.entity.*;
import matchingGoal.matchingGoal.matching.dto.CommentHistoryDto;
import matchingGoal.matchingGoal.matching.exception.NotFoundGameException;
import matchingGoal.matchingGoal.matching.repository.CommentRepository;
import matchingGoal.matchingGoal.matching.repository.GameCancelRepository;
import matchingGoal.matchingGoal.matching.repository.GameRepository;
import matchingGoal.matchingGoal.matching.repository.ResultRepository;
import matchingGoal.matchingGoal.member.dto.*;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final GameRepository gameRepository;
    private final ResultRepository resultRepository;
    private final CommentRepository commentRepository;
    private final GameCancelRepository gameCancelRepository;


    /**
     * 닉네임 중복 체크
     * @return 중복 닉네임 존재시, false 반환
     */
    public Boolean isDuplicatedNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isEmpty();
    }

    /**
     * 비밀번호 변경
     * @param passwordDto - 기존 비밀번호, 새로운 비밀번호
     * @return "변경완료"
     */
    @Transactional
    public String updatePassword(String token, UpdatePasswordDto passwordDto) {
        Member member = getMemberInfo(token);
        isMatchedPassword(passwordDto.getOldPassword(), member.getPassword());

        if(passwordDto.getOldPassword().equals(passwordDto.getNewPassword()))
            throw new CustomException(ErrorCode.PASSWORD_NOT_UPDATED);

        member.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        return "변경완료";
    }

    /**
     * 아이디를 사용하여 회원 정보 조회
     */
    public Member getMemberById(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.isDeleted())
            throw new CustomException(ErrorCode.WITHDRAWN_MEMBER);

        return member;
    }

    /**
     * 토큰을 사용하여 회원 정보 조회
     */
    public Member getMemberInfo(String token){
        jwtTokenProvider.validateToken(token);
        return getMemberById(jwtTokenProvider.getId(token));
    }

    /**
     * 개인 정보 수정
     * @param token - 토큰
     * @param updateDto - 정보 수정 dto (이름, 닉네임, 소개, 지역)
     * @return "수정완료"
     */
    @Transactional
    public String editMemberInfo(String token, UpdateMemberDto updateDto) {
        Member member = getMemberInfo(token);

        member.setName(updateDto.getName());
        member.setNickname(updateDto.getNickname());
        member.setIntroduction(updateDto.getIntroduction());
        member.setRegion(updateDto.getRegion());
        member.setImageUrl(updateDto.getImageUrl());
        return "수정완료";
    }

    /**
     * 다른 회원 정보 조회
     * @param memberId - 조회하고 싶은 회원 ID
     * @return OtherMemberInfoResponse - 닉네임, 소개, 지역, 이미지url
     */
    public SimplerInfoResponse getSimpleUserinfo(Long memberId) {
        Member member = getMemberById(memberId);
        return SimplerInfoResponse.builder()
                .nickname(member.getNickname())
                .introduction(member.getIntroduction())
                .region(member.getRegion())
                .imageUrl(member.getImageUrl())
                .build();
    }

    /**
     * 암호화된 비밀번호와 암호화되지 비밀번호 비교
     * @param rawPassword - 암호화되지 않은 비밀번호
     * @param encodedPassword - 암호화 된 비밀번호
     */
    public void isMatchedPassword(String rawPassword, String encodedPassword){
        boolean isMatch = passwordEncoder.matches(rawPassword,encodedPassword);
        if (!isMatch)
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
    }

    /**
     * 회원 일정 조회
     * @param memberId - 회원 ID
     * @param year - 년도
     * @param month - 월
     * @return 특정 월의 일정 리스트
     */
    public List<ScheduleResponse> getMemberSchedule(Long memberId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        Member member = getMemberById(memberId);

        List<ScheduleResponse> schedules = new ArrayList<>();

        List<Game> games1 = gameRepository.findByTeam1AndDateBetweenAndTeam2IsNotNull(member, startDate, endDate); // 매칭된 경기만 불러오기
        List<Game> games2 = gameRepository.findByTeam2AndDateBetween(member, startDate, endDate);
        List<Game> allGames = Stream.concat(games1.stream(), games2.stream()).toList();

        for (Game game : allGames) {
            ScheduleResponse response = ScheduleResponse.of(game, member);
            schedules.add(response);
        }
        return schedules;
    }

    /**
     * 과거 매치 목록 조회
     */
    public List<MatchHistoryResponse> getMatchHistory(Long memberId) {
        Member member = getMemberById(memberId);
        List<MatchHistoryResponse> history = new ArrayList<>();
        List<Game> allGames = gameRepository
                .findByTeam1OrTeam2AndTeam2IsNotNullAndDateLessThanEqualAndTimeLessThanOrderByDateDesc(
                        member, member, LocalDate.now(), LocalTime.now());

        for (Game game : allGames){
            try{
                Result result = resultRepository.findByGameId(game.getId()).orElseThrow(NotFoundGameException::new);
                history.add(MatchHistoryResponse.of(member, result));
            }catch (Exception e){}
        }
        return history;
    }

    /**
     * 평점, 한줄평 목록 조회
     */
    public CommentHistoryResponse getCommentHistory(Long memberId) {
        double totalRate = 0.0 ;
        Member member = getMemberById(memberId);
        List<Comment> comments = commentRepository.findByOpponent(member);
        List<CommentHistoryDto> commentDtoList = new ArrayList<>();

        for(Comment comment : comments){
            commentDtoList.add(CommentHistoryDto.of(comment));
            totalRate += comment.getScore();
        }

        int size = commentDtoList.size();

        return CommentHistoryResponse.builder()
                .totalRate(size == 0 ? 0 : totalRate / size )
                .comments(commentDtoList)
                .build();
    }

    /**
     * 경기 전적 조회
     */
    public MatchStatisticResponse getMatchStatistic(Long memberId){
        List<MatchHistoryResponse> records = getMatchHistory(memberId);

        int totalSize = records.size();
        int win = 0, lose = 0, draw = 0;
        double winRate = 0.0 ;

        for (MatchHistoryResponse record : records){
            if(record.isWin())
                win += 1;

            else{
                if(record.getScore1() == record.getScore2())
                    draw += 1;
                else
                    lose += 1;
            }
        }

        if (totalSize != 0 )
            winRate = (double) win / totalSize;

        return new MatchStatisticResponse(winRate, totalSize, win , lose ,draw );
    }

    /**
     * 참여했던 경기 횟수 조회
     */
    public MatchAttendanceResponse getMatchAttendance(Long memberId){
        int cancel = 0, noshow = 0 ;
        Member member = getMemberById(memberId);

        List<Game> allGames = gameRepository.findByTeam1OrTeam2AndTeam2IsNotNullAndDateLessThanEqualAndTimeLessThanOrderByDateDesc(member,member,LocalDate.now(),LocalTime.now());
        List<GameCancel> cancelGames = gameCancelRepository.findByMember(member);

        for (GameCancel gameCancel : cancelGames){
            if(gameCancel.getType().equals(CancelType.CANCEL))
                cancel += 1;
            else
                noshow += 1;
        }
     return new MatchAttendanceResponse(allGames.size(), cancel, noshow );
    }
}

