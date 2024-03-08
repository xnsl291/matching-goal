package matchingGoal.matchingGoal.matching.controller;

import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.matching.dto.BoardRequestDto;
import matchingGoal.matchingGoal.matching.dto.BoardResponseDto;
import matchingGoal.matchingGoal.matching.dto.UpdateBoardRequestDto;
import matchingGoal.matchingGoal.matching.service.MatchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching")
public class MatchingController {

  private final MatchingService matchingService;

  @PostMapping("/write")
  public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto requestDto) {
    return ResponseEntity.ok(matchingService.createBoard(requestDto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable Long id) {
    return ResponseEntity.ok(matchingService.getBoardById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<String> updateBoard(@PathVariable Long id, @RequestBody UpdateBoardRequestDto requestDto) {
    return ResponseEntity.ok(matchingService.updateBoard(id, requestDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteBoard(@PathVariable Long id) {
    return ResponseEntity.ok(matchingService.deleteBoard(id));
  }

  @PostMapping("/{id}/request/{memberId}")
  public ResponseEntity<String> requestMatching(@PathVariable Long id, @PathVariable Long memberId) {
    return ResponseEntity.ok(matchingService.requestMatching(id, memberId));
  }

}
