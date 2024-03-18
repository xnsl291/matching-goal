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
public class AlarmDto {
  private long id;
  private long memberId;
  private String type;
  private long contentId;
  private String message;
  private boolean checkedOut;
  private LocalDateTime createdDate;

  public static AlarmDto fromEntity(Alarm alarm){

    return AlarmDto.builder()
        .id(alarm.getId())
        .memberId(alarm.getMemberId())
        .message(alarm.getMessage())
        .type(alarm.getType())
        .contentId(alarm.getContentId())
        .checkedOut(alarm.isCheckedOut())
        .createdDate(alarm.getCreatedDate())
        .build();
  }
}
