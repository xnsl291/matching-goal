package matchingGoal.matchingGoal.matching.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.matching.dto.BoardRequestDto;
import matchingGoal.matchingGoal.matching.dto.BoardResponseDto;
import matchingGoal.matchingGoal.matching.service.MatchingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity<?> createBoard(@RequestBody BoardRequestDto requestDto) {
    matchingService.createBoard(requestDto);
    return ResponseEntity.ok().body("모집글 등록 성공");
  }

  @GetMapping("/{id}")
  public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable Long id) {
    BoardResponseDto result = matchingService.getBoardById(id);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
