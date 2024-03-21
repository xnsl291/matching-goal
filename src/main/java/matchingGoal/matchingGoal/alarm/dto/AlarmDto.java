package matchingGoal.matchingGoal.alarm.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matchingGoal.matchingGoal.alarm.domain.entity.Alarm;
import matchingGoal.matchingGoal.alarm.domain.AlarmType;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class AlarmDto {
  private long id;
  private long memberId;
  private AlarmType type;
  private String contentId;
  private String message;
  private int checkedOut;
  private LocalDateTime createdDate;

  public static AlarmDto fromEntity(Alarm alarm){

    return AlarmDto.builder()
        .id(alarm.getId())
        .memberId(alarm.getMemberId())
        .message(messageFromType(alarm.getType()))
        .type(alarm.getType())
        .contentId(alarm.getContentId())
        .checkedOut(alarm.getCheckedOut())
        .createdDate(alarm.getCreatedDate())
        .build();
  }
  public static String messageFromType(AlarmType type) {
    String result = null;
    switch (type) {
      case CHAT -> result = "새로운 채팅메세지가 있습니다";
      case NEW_MATCHING_REQUEST -> result = "새로운 매치신청이 있습니다";
      case MATCHING_REQUEST_DENIED-> result = "매치신청이 거절 되었습니다";
      case MATCHING_REQUEST_ACCEPTED -> result = "매치신청이 수락 되었습니다";
    }

    if (result == null) {
      throw new RuntimeException();
    }

    return result;
  }
}
