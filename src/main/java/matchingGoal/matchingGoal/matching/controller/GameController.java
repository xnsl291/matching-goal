package matchingGoal.matchingGoal.matching.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.matching.dto.CancelResponse;
import matchingGoal.matchingGoal.matching.dto.CommentDto;
import matchingGoal.matchingGoal.matching.dto.CommentResponse;
import matchingGoal.matchingGoal.matching.dto.ResultDto;
import matchingGoal.matchingGoal.matching.dto.ResultResponse;
import matchingGoal.matchingGoal.matching.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/game")
public class GameController {

  private final GameService gameService;

  @PostMapping("/{gameId}/result")
  public ResponseEntity<ResultResponse> writeResult(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long gameId, @Valid @RequestBody ResultDto resultDto) {
    return ResponseEntity.ok(gameService.writeResult(token, gameId, resultDto));
  }

  @PatchMapping("/result/{resultId}")
  public ResponseEntity<ResultResponse> updateResult(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long resultId,
      @Valid @RequestBody ResultDto resultDto) {
    return ResponseEntity.ok(gameService.updateResult(token, resultId, resultDto));
  }

  @PostMapping("/result/{resultId}/accept")
  public ResponseEntity<ResultResponse> acceptResult(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long resultId) {
    return ResponseEntity.ok(gameService.acceptResult(token, resultId));
  }

  @PostMapping("/result/{resultId}/refuse")
  public ResponseEntity<ResultResponse> refuseResult(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long resultId) {
    return ResponseEntity.ok(gameService.refuseResult(token, resultId));
  }

  @PostMapping("/{gameId}/comment")
  public ResponseEntity<CommentResponse> writeComment(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long gameId,
      @Valid @RequestBody CommentDto commentDto) {
    return ResponseEntity.ok(gameService.writeComment(token, gameId, commentDto));
  }

  @PostMapping("/{gameId}/cancel")
  public ResponseEntity<CancelResponse> cancelGame(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long gameId) {
    return ResponseEntity.ok(gameService.cancelGame(token, gameId));
  }

  @PostMapping("/cancel/{cancelId}/accept")
  public ResponseEntity<CancelResponse> acceptCancel(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long cancelId
  ) {
    return ResponseEntity.ok(gameService.handleCancel(token, cancelId, true));
  }

  @PostMapping("/cancel/{cancelId}/refuse")
  public ResponseEntity<CancelResponse> refuseCancel(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long cancelId
  ) {
    return ResponseEntity.ok(gameService.handleCancel(token, cancelId, false));
  }

  @PostMapping("/{gameId}/noshow")
  public ResponseEntity<?> noshowGame(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long gameId
  ) {
    return ResponseEntity.ok(gameService.noshowGame(token, gameId));
  }

}
