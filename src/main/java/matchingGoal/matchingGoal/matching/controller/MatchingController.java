package matchingGoal.matchingGoal.matching.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.matching.dto.BoardDto;
import matchingGoal.matchingGoal.matching.dto.BoardResponseDto;
import matchingGoal.matchingGoal.matching.dto.ListBoardDto;
import matchingGoal.matchingGoal.matching.dto.MatchingRequestResponseDto;
import matchingGoal.matchingGoal.matching.dto.BoardUpdateDto;
import matchingGoal.matchingGoal.matching.service.MatchingService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching")
public class MatchingController {

  private final MatchingService matchingService;

  @PostMapping("/write")
  public ResponseEntity<BoardResponseDto> createBoard(
      @RequestHeader(value = "Authorization") String token,
      @Valid @RequestBody BoardDto requestDto
  ) {
    return ResponseEntity.ok(matchingService.createBoard(token, requestDto));
  }

  @GetMapping("/list")
  public Page<ListBoardDto> getBoardList(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(name = "size") int pageSize,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String type,
      @RequestParam(defaultValue = "time") String sort,
      @RequestParam(name = "sort-direction", defaultValue = "desc") String sortDirection,
      @RequestParam(required = false) String date,
      @RequestParam(required = false) String time
  ) {
    return matchingService.getBoardList(page, pageSize, keyword, type, sort, sortDirection, date, time);
  }

  @GetMapping("/{boardId}")
  public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable Long boardId) {
    return ResponseEntity.ok(matchingService.getBoardById(boardId));
  }

  @PatchMapping("/{boardId}")
  public ResponseEntity<BoardResponseDto> updateBoard(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long boardId, @Valid @RequestBody BoardUpdateDto requestDto
  ) {
    return ResponseEntity.ok(matchingService.updateBoard(token, boardId, requestDto));
  }

  @DeleteMapping("/{boardId}")
  public ResponseEntity<Long> deleteBoard(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long boardId
  ) {
    return ResponseEntity.ok(matchingService.deleteBoard(token, boardId));
  }

  @PostMapping("/{boardId}/request")
  public ResponseEntity<String> requestMatching(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long boardId
  ) {
    return ResponseEntity.ok(matchingService.requestMatching(token, boardId));
  }

//  @GetMapping("/{boardId}/request-list")
//  public ResponseEntity<List<MatchingRequestResponseDto>> getRequestList(@PathVariable Long boardId) {
//    return ResponseEntity.ok(matchingService.getRequestList(boardId));
//  }

  @GetMapping("/{memberId}/request-list")
  public ResponseEntity<List<MatchingRequestResponseDto>> getRequestList(@PathVariable Long memberId) {
    return ResponseEntity.ok(matchingService.getRequestListByMember(memberId));
  }

  @PostMapping("/{requestId}/accept")
  public ResponseEntity<String> acceptRequest(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long requestId
  ) {
    return ResponseEntity.ok(matchingService.acceptRequest(token, requestId));
  }

  @PostMapping("/{requestId}/refuse")
  public ResponseEntity<String> refuseRequest(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable Long requestId
  ) {
    return ResponseEntity.ok(matchingService.refuseRequest(token, requestId));
  }

}
