package matchingGoal.matchingGoal.alarm.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.alarm.dto.AlarmDto;
import matchingGoal.matchingGoal.alarm.service.AlarmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm")
public class AlarmController {
  private final AlarmService alarmService;

  @PostMapping("/")
  public ResponseEntity<Long> create(@RequestHeader(value = "authorization") String token, @RequestBody AlarmDto alarmDto) {

    long result = alarmService.createAlarm(token, alarmDto);

    return ResponseEntity.ok(result);
  }

  @GetMapping("/")
  public ResponseEntity<List<AlarmDto>> myList (@RequestHeader(value = "authorization") String token) {

    List<AlarmDto> result = alarmService.getAlarm(token);

    return ResponseEntity.ok(result);
  }

  @PatchMapping("/check")
  public ResponseEntity<?> checkOut (@RequestHeader(value = "authorization") String token, @RequestBody List<Long> alarmIdList) {

    alarmService.checkOut(token, alarmIdList);

    return ResponseEntity.ok(null);
  }


}
