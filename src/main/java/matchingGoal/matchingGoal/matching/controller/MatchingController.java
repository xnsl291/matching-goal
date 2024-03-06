package matchingGoal.matchingGoal.matching.controller;

import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.matching.dto.BoardRequestDto;
import matchingGoal.matchingGoal.matching.service.MatchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matching")
public class MatchingController {

  private final MatchingService matchingService;

  @PostMapping("/write")
  public ResponseEntity<?> createBoard(@RequestBody BoardRequestDto requestDto) {
    matchingService.createBoard(requestDto);
    return ResponseEntity.ok().body("모집글 등록 성공");
  }

}
