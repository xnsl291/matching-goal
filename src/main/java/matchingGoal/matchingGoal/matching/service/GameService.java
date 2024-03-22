package matchingGoal.matchingGoal.matching.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import matchingGoal.matchingGoal.matching.domain.CancelType;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.domain.entity.Comment;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.GameCancel;
import matchingGoal.matchingGoal.matching.domain.entity.Result;
import matchingGoal.matchingGoal.matching.dto.CancelResponse;
import matchingGoal.matchingGoal.matching.dto.CommentDto;
import matchingGoal.matchingGoal.matching.dto.CommentResponse;
import matchingGoal.matchingGoal.matching.dto.ResultDto;
import matchingGoal.matchingGoal.matching.dto.ResultResponse;
import matchingGoal.matchingGoal.matching.exception.AcceptedCancelException;
import matchingGoal.matchingGoal.matching.exception.AcceptedResultException;
import matchingGoal.matchingGoal.matching.exception.CancelTimeException;
import matchingGoal.matchingGoal.matching.exception.ExistingCommentException;
import matchingGoal.matchingGoal.matching.exception.ExistingResultException;
import matchingGoal.matchingGoal.matching.exception.NotFoundCancelException;
import matchingGoal.matchingGoal.matching.exception.NotFoundGameException;
import matchingGoal.matchingGoal.matching.exception.NotFoundMemberException;
import matchingGoal.matchingGoal.matching.exception.NotFoundResultException;
import matchingGoal.matchingGoal.matching.exception.PermissionException;
import matchingGoal.matchingGoal.matching.repository.CommentRepository;
import matchingGoal.matchingGoal.matching.repository.GameCancelRepository;
import matchingGoal.matchingGoal.matching.repository.GameRepository;
import matchingGoal.matchingGoal.matching.repository.ResultRepository;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameService {

  private final GameRepository gameRepository;
  private final MemberRepository memberRepository;
  private final ResultRepository resultRepository;
  private final CommentRepository commentRepository;
  private final GameCancelRepository cancelRepository;
  private final JwtTokenProvider jwtTokenProvider;

  public ResultResponse writeResult(String token, Long gameId, ResultDto resultDto) {
    Member winner = memberRepository.findById(resultDto.getWinnerId())
        .orElseThrow(NotFoundMemberException::new);
    Game game = gameRepository.findById(gameId)
        .orElseThrow(NotFoundGameException::new);

    checkMemberPermission(token, game.getTeam2());

    if (!resultDto.getWinnerId().equals(game.getTeam1().getId())
        && !resultDto.getWinnerId().equals(game.getTeam2().getId())) {
      throw new NonParticipatingException();
    }

    if (resultRepository.existsByGame(game)) {
      throw new ExistingResultException();
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

  @Transactional
  public ResultResponse updateResult(String token, Long resultId, ResultDto resultDto) {
    Result result = resultRepository.findById(resultId)
        .orElseThrow(NotFoundResultException::new);

    checkMemberPermission(token, result.getGame().getTeam2());

    Member winner = memberRepository.findById(resultDto.getWinnerId())
        .orElseThrow(NotFoundMemberException::new);

    if (Boolean.TRUE.equals(result.getIsAccepted())) {
      throw new AcceptedResultException();
    }

    result.setWinner(winner);
    result.update(resultDto);
    return ResultResponse.of(result);
  }

  @Transactional
  public ResultResponse acceptResult(String token, Long resultId) {
    Result result = resultRepository.findById(resultId)
        .orElseThrow(NotFoundResultException::new);

    checkMemberPermission(token, result.getGame().getTeam1());

    if (Boolean.TRUE.equals(result.getIsAccepted())) {
      throw new AcceptedResultException();
    }

    result.setIsAccepted(true);

    return ResultResponse.of(result);
  }

  @Transactional
  public ResultResponse refuseResult(String token, Long resultId) {
    Result result = resultRepository.findById(resultId)
        .orElseThrow(NotFoundResultException::new);

    checkMemberPermission(token, result.getGame().getTeam1());

    if (Boolean.TRUE.equals(result.getIsAccepted())) {
      throw new AcceptedResultException();
    }

    result.setIsAccepted(false);

    return ResultResponse.of(result);
  }

  public CommentResponse writeComment(String token, Long gameId, CommentDto commentDto) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(NotFoundGameException::new);

    Member member = getMemberByToken(token);
    Member opponent;
    if (game.getTeam1() == member) {
      opponent = game.getTeam2();
    } else if (game.getTeam2() == member) {
      opponent = game.getTeam1();
    } else {
      throw new PermissionException();
    }

    if (commentRepository.existsByGame(game)) {
      throw new ExistingCommentException();
    }

    Comment comment = Comment.builder()
        .opponent(opponent)
        .writer(member)
        .game(game)
        .content(commentDto.getContent())
        .score(commentDto.getScore())
        .createdDate(LocalDateTime.now())
        .isDeleted(false)
        .build();
    commentRepository.save(comment);

    return CommentResponse.of(comment);
  }

  public CancelResponse cancelGame(String token, Long gameId) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(NotFoundGameException::new);

    Member member = getMemberByToken(token);
    if (game.getTeam1() != member && game.getTeam2() != member) {
      throw new PermissionException();
    }

    LocalDateTime gameDateTime = LocalDateTime.of(game.getDate(), game.getTime());
    if (LocalDateTime.now().isAfter(gameDateTime.minusHours(24))) {
      throw new CancelTimeException();
    }

    GameCancel cancel = GameCancel.builder()
        .member(member)
        .game(game)
        .type(null)
        .isAgreed(null)
        .createdDate(LocalDateTime.now())
        .build();
    cancelRepository.save(cancel);

    return CancelResponse.of(cancel);
  }

  @Transactional
  public CancelResponse handleCancel(String token, Long cancelId, boolean isAgreed) {
    GameCancel cancel = cancelRepository.findById(cancelId)
        .orElseThrow(NotFoundCancelException::new);
    Game game = cancel.getGame();

    Member acceptMember = cancel.getMember().equals(game.getTeam1()) ? game.getTeam2() : game.getTeam1();
    checkMemberPermission(token, acceptMember);

    if (Boolean.TRUE.equals(cancel.getIsAgreed())) {
      throw new AcceptedCancelException();
    }

    cancel.setIsAgreed(isAgreed);
    cancel.setType(CancelType.CANCEL);
    game.getBoard().setStatus(StatusType.CANCELLED);

    return CancelResponse.of(cancel);
  }

  private Member getMemberByToken(String token) {
    jwtTokenProvider.validateToken(token);
    return memberRepository.findById(jwtTokenProvider.getId(token))
        .orElseThrow(NotFoundMemberException::new);
  }

  private void checkMemberPermission(String token, Member allowed) {
    Member member = getMemberByToken(token);
    if (allowed != member) {
      throw new PermissionException();
    }
  }
}
