package matchingGoal.matchingGoal.matching.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.matching.domain.CancelType;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.domain.entity.Comment;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.GameCancel;
import matchingGoal.matchingGoal.matching.domain.entity.Result;
import matchingGoal.matchingGoal.matching.dto.CancelResponseDto;
import matchingGoal.matchingGoal.matching.dto.CommentDto;
import matchingGoal.matchingGoal.matching.dto.CommentHistoryDto;
import matchingGoal.matchingGoal.matching.dto.ResultDto;
import matchingGoal.matchingGoal.matching.dto.ResultResponseDto;
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

  public ResultResponseDto writeResult(String token, Long gameId, ResultDto resultDto) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new CustomException(ErrorCode.GAME_NOT_FOUND));

    LocalDateTime gameDateTime = LocalDateTime.of(game.getDate(), game.getTime());
    if (gameDateTime.isAfter(LocalDateTime.now())) {
      throw new CustomException(ErrorCode.NOT_AVAILABLE_TIME);
    }

//    memberService.checkMemberPermission(token, game.getTeam2());
    Member member = memberService.getMemberInfo(token);
    if (game.getTeam1() != member && game.getTeam2() != member) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    if (resultRepository.existsByGame(game)) {
      throw new CustomException(ErrorCode.ALREADY_RESULT_EXISTS);
    }

    Result result = Result.builder()
        .game(game)
        .score1(resultDto.getScore1())
        .score2(resultDto.getScore2())
        .duration(resultDto.getDuration())
//        .isAccepted(null)
        .build();
    resultRepository.save(result);

    return ResultResponseDto.of(result);
  }

//  @Transactional
//  public ResultResponseDto updateResult(String token, Long resultId, ResultDto resultDto) {
//    Result result = resultRepository.findById(resultId)
//        .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_FOUND));
//
//    memberService.checkMemberPermission(token, result.getGame().getTeam2());
//
//    if (Boolean.TRUE.equals(result.getIsAccepted())) {
//      throw new CustomException(ErrorCode.ALREADY_ACCEPTED_RESULT);
//    }
//
//    result.update(resultDto);
//    return ResultResponseDto.of(result);
//  }
//
//  @Transactional
//  public ResultResponseDto handleResult(String token, Long resultId, boolean isAccepted) {
//    Result result = resultRepository.findById(resultId)
//        .orElseThrow(() -> new CustomException(ErrorCode.RESULT_NOT_FOUND));
//
//    memberService.checkMemberPermission(token, result.getGame().getTeam1());
//
//    if (Boolean.TRUE.equals(result.getIsAccepted())) {
//      throw new CustomException(ErrorCode.ALREADY_ACCEPTED_RESULT);
//    }
//
//    result.setIsAccepted(isAccepted);
//
//    return ResultResponseDto.of(result);
//  }

  public CommentHistoryDto writeComment(String token, Long gameId, CommentDto commentDto) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new CustomException(ErrorCode.GAME_NOT_FOUND));

    Member member = memberService.getMemberInfo(token);
    Member opponent;
    if (game.getTeam1() == member) {
      opponent = game.getTeam2();
    } else if (game.getTeam2() == member) {
      opponent = game.getTeam1();
    } else {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    if (commentRepository.existsByGame(game)) {
      throw new CustomException(ErrorCode.ALREADY_COMMENT_EXISTS);
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

    return CommentHistoryDto.of(comment);
  }

  public CancelResponseDto cancelGame(String token, Long gameId) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new CustomException(ErrorCode.GAME_NOT_FOUND));

    Member member = memberService.getMemberInfo(token);
    if (game.getTeam1() != member && game.getTeam2() != member) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    LocalDateTime gameDateTime = LocalDateTime.of(game.getDate(), game.getTime());
    if (LocalDateTime.now().isAfter(gameDateTime.minusHours(24))) {
      throw new CustomException(ErrorCode.NOT_AVAILABLE_TIME);
    }

    GameCancel cancel = GameCancel.builder()
        .member(member)
        .game(game)
        .type(CancelType.CANCEL)
        .isAgreed(null)
        .createdDate(LocalDateTime.now())
        .build();
    cancelRepository.save(cancel);

    return CancelResponseDto.of(cancel);
  }

  public String noshowGame(String token, Long gameId) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new CustomException(ErrorCode.GAME_NOT_FOUND));

    Member member = memberService.getMemberInfo(token);
    if (game.getTeam1() != member && game.getTeam2() != member) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    LocalDateTime gameDateTime = LocalDateTime.of(game.getDate(), game.getTime());
    if (LocalDateTime.now().isBefore(gameDateTime.plusMinutes(10))) {
      throw new CustomException(ErrorCode.NOT_AVAILABLE_TIME);
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
  public CancelResponseDto handleCancel(String token, Long cancelId, boolean isAgreed) {
    GameCancel cancel = cancelRepository.findById(cancelId)
        .orElseThrow(() -> new CustomException(ErrorCode.CANCEL_NOT_FOUND));
    Game game = cancel.getGame();

    Member acceptMember = cancel.getMember().equals(game.getTeam1()) ? game.getTeam2() : game.getTeam1();
    memberService.checkMemberPermission(token, acceptMember);

    if (Boolean.TRUE.equals(cancel.getIsAgreed())) {
      throw new CustomException(ErrorCode.ALREADY_ACCEPTED_CANCEL);
    }

    cancel.setIsAgreed(isAgreed);
    game.getBoard().setStatus(StatusType.CANCELLED);

    return CancelResponseDto.of(cancel);
  }

}
