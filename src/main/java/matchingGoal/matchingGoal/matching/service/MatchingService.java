package matchingGoal.matchingGoal.matching.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.matching.exception.AlreadyRequestException;
import matchingGoal.matchingGoal.matching.exception.SelfRequestException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingRequest;
import matchingGoal.matchingGoal.matching.dto.BoardRequestDto;
import matchingGoal.matchingGoal.matching.dto.BoardResponseDto;
import matchingGoal.matchingGoal.matching.dto.UpdateBoardRequestDto;
import matchingGoal.matchingGoal.matching.exception.NotFoundGameException;
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
        .isDeleted(Boolean.FALSE)
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

    return BoardResponseDto.convertToDto(matchingBoard);
  }

  /**
   * 게시글 수정
   * @param id - 게시글 id
   * @param requestDto - 게시글 수정 dto
   * @return "게시글 수정 완료"
   */
  public String updateBoard(Long id, UpdateBoardRequestDto requestDto) {
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

    if (requestRepository.existsByBoardIdAndMemberId(matchingBoard, member)) {
      throw new AlreadyRequestException(ErrorCode.ALREADY_REQUEST_MATCHING);
    }

    MatchingRequest matchingRequest = MatchingRequest.builder()
        .board(matchingBoard)
        .member(member)
        .isAccepted(Boolean.FALSE)
        .build();
    requestRepository.save(matchingRequest);

    return "매칭 신청 완료";
  }

}
