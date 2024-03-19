package matchingGoal.matchingGoal.matching.service;

import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.Result;
import matchingGoal.matchingGoal.matching.dto.ResultDto;
import matchingGoal.matchingGoal.matching.dto.ResultResponse;
import matchingGoal.matchingGoal.matching.exception.AcceptedResultException;
import matchingGoal.matchingGoal.matching.exception.NonParticipatingException;
import matchingGoal.matchingGoal.matching.exception.NotFoundGameException;
import matchingGoal.matchingGoal.matching.exception.NotFoundMemberException;
import matchingGoal.matchingGoal.matching.exception.NotFoundResultException;
import matchingGoal.matchingGoal.matching.exception.PermissionException;
import matchingGoal.matchingGoal.matching.repository.GameRepository;
import matchingGoal.matchingGoal.matching.repository.ResultRepository;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

  private final GameRepository gameRepository;
  private final MemberRepository memberRepository;
  private final ResultRepository resultRepository;
  private final JwtTokenProvider jwtTokenProvider;

  public ResultResponse writeResult(String token, Long gameId, ResultDto resultDto) {
    Member winner = memberRepository.findById(resultDto.getWinnerId())
        .orElseThrow(NotFoundMemberException::new);
    Game game = gameRepository.findById(gameId)
        .orElseThrow(NotFoundGameException::new);

    Member member = getMemberByToken(token);
    if (game.getTeam2() != member) {
      throw new PermissionException();
    }

    if (!resultDto.getWinnerId().equals(game.getTeam1().getId())
        && !resultDto.getWinnerId().equals(game.getTeam2().getId())) {
      throw new NonParticipatingException();
    }

    if (resultRepository.existsByGame(game)) {
      return ResultResponse.of(updateResultByGame(game, resultDto));
    }

    Result result = Result.builder()
        .game(game)
        .winner(winner)
        .score1(resultDto.getScore1())
        .score2(resultDto.getScore2())
        .duration(resultDto.getDuration())
        .isAccepted(null)
        .build();
    resultRepository.save(result);

    return ResultResponse.of(result);
  }

  private Result updateResultByGame(Game game, ResultDto resultDto) {
    Result result = resultRepository.findByGame(game)
        .orElseThrow(NotFoundResultException::new);

    Member winner = memberRepository.findById(resultDto.getWinnerId())
        .orElseThrow(NotFoundMemberException::new);

    if (result.getIsAccepted() != null && result.getIsAccepted() == true) {
      throw new AcceptedResultException();
    }

    result.setWinner(winner);
    result.update(resultDto);
    return resultRepository.save(result);
  }

  public ResultResponse acceptResult(String token, Long resultId) {
    Result result = resultRepository.findById(resultId)
        .orElseThrow(NotFoundResultException::new);

    Member member = getMemberByToken(token);
    if (result.getGame().getTeam1() != member) {
      throw new PermissionException();
    }

    if (result.getIsAccepted() != null && result.getIsAccepted() == true) {
      throw new AcceptedResultException();
    }

    result.setIsAccepted(true);
    resultRepository.save(result);

    return ResultResponse.of(result);
  }

  public ResultResponse refuseResult(String token, Long resultId) {
    Result result = resultRepository.findById(resultId)
        .orElseThrow(NotFoundResultException::new);

    Member member = getMemberByToken(token);
    if (result.getGame().getTeam1() != member) {
      throw new PermissionException();
    }

    if (result.getIsAccepted() != null && result.getIsAccepted() == true) {
      throw new AcceptedResultException();
    }

    result.setIsAccepted(false);
    resultRepository.save(result);

    return ResultResponse.of(result);
  }

  private Member getMemberByToken(String token) {
    jwtTokenProvider.validateToken(token);
    return memberRepository.findById(jwtTokenProvider.getId(token))
        .orElseThrow(NotFoundMemberException::new);
  }
}
