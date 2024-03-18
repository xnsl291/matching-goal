package matchingGoal.matchingGoal.alarm.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.alarm.dto.AlarmDto;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private long memberId;
  @Convert(converter = AlarmTypeAttributeConverter.class)
  private String type;
  private long contentId;
  private String message;
  private boolean checkedOut;
  @CreatedDate
  private LocalDateTime createdDate;

  public static Alarm fromDto(AlarmDto dto) {
    return Alarm.builder()
        .memberId(dto.getMemberId())
        .type(dto.getType())
        .contentId(dto.getContentId())
        .message(dto.getMessage())
        .checkedOut(dto.isCheckedOut())
        .build();
  }
}

