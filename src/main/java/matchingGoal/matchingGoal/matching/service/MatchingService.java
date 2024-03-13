package matchingGoal.matchingGoal.matching.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.matching.dto.RequestMatchingDto;
import matchingGoal.matchingGoal.matching.exception.AlreadyRequestException;
import matchingGoal.matchingGoal.matching.exception.NotFoundRequestException;
import matchingGoal.matchingGoal.matching.exception.SelfRequestException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingRequest;
import matchingGoal.matchingGoal.matching.dto.BoardRequestDto;
import matchingGoal.matchingGoal.matching.dto.BoardResponseDto;
import matchingGoal.matchingGoal.matching.dto.UpdateBoardDto;
import matchingGoal.matchingGoal.matching.exception.NotFoundMemberException;
import matchingGoal.matchingGoal.matching.exception.NotFoundPostException;
import matchingGoal.matchingGoal.matching.repository.GameRepository;
import matchingGoal.matchingGoal.matching.repository.MatchingBoardRepository;
import matchingGoal.matchingGoal.matching.repository.MatchingRequestRepository;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingService {

  private final MatchingBoardRepository boardRepository;
  private final GameRepository gameRepository;
  private final MemberRepository memberRepository;
  private final MatchingRequestRepository requestRepository;

  /**
   * 게시글 작성
   * @param requestDto - 게시글 작성 dto
   */
  @Transactional
  public String createBoard(BoardRequestDto requestDto) {
    Member member = memberRepository.findById(requestDto.getMemberId())
        .orElseThrow(() -> new NotFoundMemberException(ErrorCode.MEMBER_NOT_FOUND));

    MatchingBoard matchingBoard = MatchingBoard.builder()
        .member(member)
        .region(requestDto.getRegion())
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .status(StatusType.모집중)
        .createdDate(LocalDateTime.now())
        .isDeleted(false)
        .build();
    MatchingBoard savedBoard = boardRepository.save(matchingBoard);

    Game game = Game.builder()
        .board(savedBoard)
        .team1(member)
        .team2(null)
        .stadiumName(requestDto.getStadium())
        .date(LocalDate.parse(requestDto.getDate()))
        .time(LocalTime.parse(requestDto.getTime()))
        .build();
    gameRepository.save(game);

    return "게시글 등록 완료";
  }

  /**
   * 게시글 조회
   * @param id - 게시글 id
   * @return 게시글 dto
   */
  public BoardResponseDto getBoardById(Long id) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));

    return BoardResponseDto.of(matchingBoard);
  }

  /**
   * 게시글 수정
   * @param id - 게시글 id
   * @param requestDto - 게시글 수정 dto
   * @return "게시글 수정 완료"
   */
  public String updateBoard(Long id, UpdateBoardDto requestDto) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));

    matchingBoard.update(requestDto);

    boardRepository.save(matchingBoard);

    return "게시글 수정 완료";
  }

  /**
   * 게시글 삭제
   * @param id - 게시글 id
   * @return "게시글 삭제 완료"
   */
  @Transactional
  public String deleteBoard(Long id) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));

    gameRepository.delete(matchingBoard.getGame());
    boardRepository.delete(matchingBoard);

    return "게시글 삭제 완료";
  }

  /**
   * 경기 매칭 신청
   * @param id - 게시글 id
   * @param memberId - 신청자 id
   * @return "매칭 신청 완료"
   */
  public String requestMatching(Long id, Long memberId) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new NotFoundMemberException(ErrorCode.MEMBER_NOT_FOUND));

    if (matchingBoard.getMember() == member) {
      throw new SelfRequestException(ErrorCode.SELF_REQUEST);
    }

    if (requestRepository.existsByBoardIdAndMemberId(id, memberId)) {
      throw new AlreadyRequestException(ErrorCode.ALREADY_REQUEST_MATCHING);
    }

    MatchingRequest matchingRequest = MatchingRequest.builder()
        .board(matchingBoard)
        .member(member)
        .isAccepted(false)
        .createdDate(LocalDateTime.now())
        .build();
    requestRepository.save(matchingRequest);

    return "매칭 신청 완료";
  }

  /**
   * 매칭 신청한 팀 리스트 조회
   * @param id - 게시글 id
   * @return 매칭 신청 목록
   */
  public List<RequestMatchingDto> getRequestList(Long id) {
    List<MatchingRequest> requestList = requestRepository.findByBoardId(id);

    return requestList.stream().map(RequestMatchingDto::of)
        .sorted(Comparator.comparing(RequestMatchingDto::getCreatedDate))
        .collect(Collectors.toList());
  }

  /**
   * 매칭 신청 수락
   * @param id - 매칭 신청 id
   * @return "신청 수락 완료"
   */
  public String acceptRequest(Long id) {
    MatchingRequest request = requestRepository.findById(id)
        .orElseThrow(() -> new NotFoundRequestException(ErrorCode.REQUEST_NOT_FOUND));

    request.accept();

    return "신청 수락 완료";
  }

}
