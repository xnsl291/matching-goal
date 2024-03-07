package matchingGoal.matchingGoal.matching.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.common.exception.NotFoundGameException;
import matchingGoal.matchingGoal.common.exception.NotFoundMemberException;
import matchingGoal.matchingGoal.common.exception.NotFoundPostException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import matchingGoal.matchingGoal.matching.dto.BoardRequestDto;
import matchingGoal.matchingGoal.matching.dto.BoardResponseDto;
import matchingGoal.matchingGoal.matching.repository.GameRepository;
import matchingGoal.matchingGoal.matching.repository.MatchingBoardRepository;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingService {

  private final MatchingBoardRepository matchingBoardRepository;
  private final GameRepository gameRepository;
  private final MemberRepository memberRepository;

  public void createBoard(BoardRequestDto requestDto) {
    Member member = memberRepository.findById(requestDto.getMemberId())
        .orElseThrow(() -> new NotFoundMemberException(ErrorCode.MEMBER_NOT_FOUND));

    MatchingBoard matchingBoard = MatchingBoard.builder()
        .memberId(member)
        .region(requestDto.getRegion())
        .capacity(requestDto.getCapacity())
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .status(StatusType.모집중)
        .createdDate(LocalDateTime.now())
        .build();
    MatchingBoard savedBoard = matchingBoardRepository.save(matchingBoard);

    Game game = Game.builder()
        .boardId(savedBoard)
        .team1Id(member)
        .team2Id(null)
        .stadiumName(requestDto.getStadium())
        .time(LocalDateTime.parse(requestDto.getTime()))
        .build();
    gameRepository.save(game);
  }

  public BoardResponseDto getBoardById(Long id) {
    MatchingBoard matchingBoard = matchingBoardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));
    Game game = gameRepository.findByBoardId_Id(id)
        .orElseThrow(() -> new NotFoundGameException(ErrorCode.GAME_NOT_FOUND));

    return BoardResponseDto.convertToDto(matchingBoard, game);
  }
}
