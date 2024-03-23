package matchingGoal.matchingGoal.matching.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
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
import matchingGoal.matchingGoal.matching.exception.ExistingCommentException;
import matchingGoal.matchingGoal.matching.exception.ExistingResultException;
import matchingGoal.matchingGoal.matching.exception.NotAvailableTimeException;
import matchingGoal.matchingGoal.matching.exception.NotFoundCancelException;
import matchingGoal.matchingGoal.matching.exception.NotFoundGameException;
import matchingGoal.matchingGoal.matching.exception.NotFoundResultException;
import matchingGoal.matchingGoal.matching.exception.PermissionException;
import matchingGoal.matchingGoal.matching.repository.CommentRepository;
import matchingGoal.matchingGoal.matching.repository.GameCancelRepository;
import matchingGoal.matchingGoal.matching.repository.GameRepository;
import matchingGoal.matchingGoal.matching.repository.ResultRepository;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameService {

  private final GameRepository gameRepository;
  private final ResultRepository resultRepository;
  private final CommentRepository commentRepository;
  private final GameCancelRepository cancelRepository;
  private final MemberService memberService;

  public ResultResponse writeResult(String token, Long gameId, ResultDto resultDto) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(NotFoundGameException::new);

    memberService.checkMemberPermission(token, game.getTeam2());

    if (resultRepository.existsByGame(game)) {
      throw new ExistingResultException();
    }

    Result result = Result.builder()
        .game(game)
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

    memberService.checkMemberPermission(token, result.getGame().getTeam2());

    if (Boolean.TRUE.equals(result.getIsAccepted())) {
      throw new AcceptedResultException();
    }

    result.update(resultDto);
    return ResultResponse.of(result);
  }

  @Transactional
  public ResultResponse acceptResult(String token, Long resultId) {
    Result result = resultRepository.findById(resultId)
        .orElseThrow(NotFoundResultException::new);

    memberService.checkMemberPermission(token, result.getGame().getTeam1());

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

    memberService.checkMemberPermission(token, result.getGame().getTeam1());

    if (Boolean.TRUE.equals(result.getIsAccepted())) {
      throw new AcceptedResultException();
    }

    result.setIsAccepted(false);

    return ResultResponse.of(result);
  }

  public CommentResponse writeComment(String token, Long gameId, CommentDto commentDto) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(NotFoundGameException::new);

    Member member = memberService.getMemberInfo(token);
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

    Member member = memberService.getMemberInfo(token);
    if (game.getTeam1() != member && game.getTeam2() != member) {
      throw new PermissionException();
    }

    LocalDateTime gameDateTime = LocalDateTime.of(game.getDate(), game.getTime());
    if (LocalDateTime.now().isAfter(gameDateTime.minusHours(24))) {
      throw new NotAvailableTimeException();
    }

    GameCancel cancel = GameCancel.builder()
        .member(member)
        .game(game)
        .type(CancelType.CANCEL)
        .isAgreed(null)
        .createdDate(LocalDateTime.now())
        .build();
    cancelRepository.save(cancel);

    return CancelResponse.of(cancel);
  }

  public String noshowGame(String token, Long gameId) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(NotFoundGameException::new);

    Member member = memberService.getMemberInfo(token);
    if (game.getTeam1() != member && game.getTeam2() != member) {
      throw new PermissionException();
    }

    LocalDateTime gameDateTime = LocalDateTime.of(game.getDate(), game.getTime());
    if (LocalDateTime.now().isBefore(gameDateTime.plusMinutes(10))) {
      throw new NotAvailableTimeException();
    }

    Member noshowMember = member.equals(game.getTeam1()) ? game.getTeam2() : game.getTeam1();

    GameCancel cancel = GameCancel.builder()
        .member(noshowMember)
        .game(game)
        .type(CancelType.NO_SHOW)
        .isAgreed(false)
        .createdDate(LocalDateTime.now())
        .build();
    cancelRepository.save(cancel);

    return "노쇼 처리 완료";
  }

  @Transactional
  public CancelResponse handleCancel(String token, Long cancelId, boolean isAgreed) {
    GameCancel cancel = cancelRepository.findById(cancelId)
        .orElseThrow(NotFoundCancelException::new);
    Game game = cancel.getGame();

    Member acceptMember = cancel.getMember().equals(game.getTeam1()) ? game.getTeam2() : game.getTeam1();
    memberService.checkMemberPermission(token, acceptMember);

    if (Boolean.TRUE.equals(cancel.getIsAgreed())) {
      throw new AcceptedCancelException();
    }

    cancel.setIsAgreed(isAgreed);
    game.getBoard().setStatus(StatusType.CANCELLED);

    return CancelResponse.of(cancel);
  }

}
