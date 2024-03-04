package matchingGoal.matchingGoal.matching.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import matchingGoal.matchingGoal.matching.dto.BoardRequestDto;
import matchingGoal.matchingGoal.matching.repository.MatchRepository;
import matchingGoal.matchingGoal.matching.repository.MatchingRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingService {

  private final MatchingRepository matchingRepository;
  private final MatchRepository matchRepository;

  public void createBoard(BoardRequestDto requestDto) {
    MatchingBoard matchingBoard = MatchingBoard.builder()
        .memberId(requestDto.getMemberId())   // todo: member 변경
        .region(requestDto.getRegion())
        .capacity(requestDto.getCapacity())
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .status(StatusType.모집중)
        .createdDate(LocalDateTime.now())
        .build();
    MatchingBoard savedBoard = matchingRepository.save(matchingBoard);

    Game game = Game.builder()
        .boardId(savedBoard.getId())
        .team1Id(requestDto.getMemberId())
        .team2Id(null)
        .stadiumName(requestDto.getStadium())
        .time(LocalDateTime.parse(requestDto.getTime()))
        .build();
    Game savedGame = matchRepository.save(game);
  }

}
