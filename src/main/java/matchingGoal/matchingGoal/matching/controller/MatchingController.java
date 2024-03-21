package matchingGoal.matchingGoal.matching.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.matching.dto.BoardRequestDto;
import matchingGoal.matchingGoal.matching.dto.BoardResponseDto;
import matchingGoal.matchingGoal.matching.dto.ListBoardDto;
import matchingGoal.matchingGoal.matching.dto.RequestMatchingDto;
import matchingGoal.matchingGoal.matching.dto.UpdateBoardDto;
import matchingGoal.matchingGoal.matching.service.MatchingService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching")
public class MatchingController {

  private final MatchingService matchingService;

  @PostMapping("/write")
  public ResponseEntity<BoardResponseDto> createBoard(@Valid @RequestBody BoardRequestDto requestDto) {
    return ResponseEntity.ok(matchingService.createBoard(requestDto));
  }

  @GetMapping("/list")
  public Page<ListBoardDto> getBoardList(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String type,
      @RequestParam(defaultValue = "time") String sort,
      @RequestParam(name = "sort-direction", defaultValue = "desc") String sortDirection,
      @RequestParam(required = false) String date,
      @RequestParam(required = false) String time
  ) {
    return matchingService.getBoardList(page, keyword, type, sort, sortDirection, date, time);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable Long id) {
    return ResponseEntity.ok(matchingService.getBoardById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long id, @Valid @RequestBody UpdateBoardDto requestDto) {
    return ResponseEntity.ok(matchingService.updateBoard(id, requestDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Long> deleteBoard(@PathVariable Long id) {
    return ResponseEntity.ok(matchingService.deleteBoard(id));
  }

  @PostMapping("/{id}/request/{memberId}")
  public ResponseEntity<String> requestMatching(@PathVariable Long id, @PathVariable Long memberId) {
    return ResponseEntity.ok(matchingService.requestMatching(id, memberId));
  }

  @GetMapping("/{id}/request-list")
  public ResponseEntity<List<RequestMatchingDto>> getRequestList(@PathVariable Long id) {
    return ResponseEntity.ok(matchingService.getRequestList(id));
  }

  @PostMapping("/{id}/accept")
  public ResponseEntity<String> acceptRequest(@PathVariable Long id) {
    return ResponseEntity.ok(matchingService.acceptRequest(id));
  }

  @PostMapping("/{id}/refuse")
  public ResponseEntity<String> refuseRequest(@PathVariable Long id) {
    return ResponseEntity.ok(matchingService.refuseRequest(id));
  }

}
