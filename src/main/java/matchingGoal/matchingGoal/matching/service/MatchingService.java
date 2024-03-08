package matchingGoal.matchingGoal.matching.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.matching.exception.NotFoundGameException;
import matchingGoal.matchingGoal.matching.exception.NotFoundMemberException;
import matchingGoal.matchingGoal.matching.exception.NotFoundPostException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import matchingGoal.matchingGoal.matching.dto.BoardRequestDto;
import matchingGoal.matchingGoal.matching.dto.BoardResponseDto;
import matchingGoal.matchingGoal.matching.dto.UpdateBoardRequestDto;
import matchingGoal.matchingGoal.matching.repository.GameRepository;
import matchingGoal.matchingGoal.matching.repository.MatchingBoardRepository;
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

  /**
   * 게시글 작성
   * @param requestDto - 게시글작성 dto
   */
  @Transactional
  public BoardResponseDto createBoard(BoardRequestDto requestDto) {
    Member member = memberRepository.findById(requestDto.getMemberId())
        .orElseThrow(() -> new NotFoundMemberException(ErrorCode.MEMBER_NOT_FOUND));

    MatchingBoard matchingBoard = MatchingBoard.builder()
        .memberId(member)
        .region(requestDto.getRegion())
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .status(StatusType.모집중)
        .createdDate(LocalDateTime.now())
        .build();
    MatchingBoard savedBoard = boardRepository.save(matchingBoard);

    Game game = Game.builder()
        .boardId(savedBoard)
        .team1Id(member)
        .team2Id(null)
        .stadiumName(requestDto.getStadium())
        .date(LocalDate.parse(requestDto.getDate()))
        .time(LocalTime.parse(requestDto.getTime()))
        .build();
    gameRepository.save(game);

    return BoardResponseDto.convertToDto(savedBoard, game);
  }

  public BoardResponseDto getBoardById(Long id) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));
    Game game = gameRepository.findByBoardId_Id(id)
        .orElseThrow(() -> new NotFoundGameException(ErrorCode.GAME_NOT_FOUND));

    return BoardResponseDto.convertToDto(matchingBoard, game);
  }

  public String updateBoard(Long id, UpdateBoardRequestDto requestDto) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));

    matchingBoard.update(requestDto);

    boardRepository.save(matchingBoard);

    return "게시글 수정 완료";
  }

  @Transactional
  public String deleteBoard(Long id) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));
    Game game = gameRepository.findByBoardId_Id(id)
        .orElseThrow(() -> new NotFoundGameException(ErrorCode.GAME_NOT_FOUND));

    gameRepository.delete(game);
    boardRepository.delete(matchingBoard);

    return "게시글 삭제 완료";
  }
}
