package matchingGoal.matchingGoal.alarm.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matchingGoal.matchingGoal.alarm.entity.Alarm;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmResponse {
  private long id;
  private long memberId;
  private String message;
  private String url;
  private boolean checkedOut;
  private LocalDateTime createdDate;

  public AlarmResponse fromEntity (Alarm alarm){

    return AlarmResponse.builder()
        .id(alarm.getId())
        .memberId(alarm.getMemberId())
        .message(alarm.getMessage())
        .url(alarm.getUrl())
        .checkedOut(alarm.isCheckedOut())
        .createdDate(alarm.getCreatedDate())
        .build();
  }
}
