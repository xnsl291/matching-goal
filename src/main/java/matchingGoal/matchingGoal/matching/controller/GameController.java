package matchingGoal.matchingGoal.matching.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.matching.dto.ResultDto;
import matchingGoal.matchingGoal.matching.dto.ResultResponse;
import matchingGoal.matchingGoal.matching.service.GameService;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<ResultResponse> writeResult(@RequestHeader(value = "Authorization") String token, @PathVariable Long gameId, @Valid @RequestBody ResultDto resultDto) {
    return ResponseEntity.ok(gameService.writeResult(token, gameId, resultDto));
  }

  @PostMapping("/result/{resultId}/accept")
  public ResponseEntity<ResultResponse> acceptResult(@RequestHeader(value = "Authorization") String token, @PathVariable Long resultId) {
    return ResponseEntity.ok(gameService.acceptResult(token, resultId));
  }

  @PostMapping("/result/{resultId}/refuse")
  public ResponseEntity<ResultResponse> refuseResult(@RequestHeader(value = "Authorization") String token, @PathVariable Long resultId) {
    return ResponseEntity.ok(gameService.refuseResult(token, resultId));
  }

}
