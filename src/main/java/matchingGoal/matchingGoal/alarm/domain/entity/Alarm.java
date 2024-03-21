package matchingGoal.matchingGoal.alarm.domain.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.alarm.domain.AlarmType;
import matchingGoal.matchingGoal.alarm.domain.AlarmTypeAttributeConverter;
import matchingGoal.matchingGoal.alarm.dto.AlarmDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Alarm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private long memberId;
  @Convert(converter = AlarmTypeAttributeConverter.class)
  private AlarmType type;
  private long contentId;
  @Default
  private boolean checkedOut = false;
  @CreatedDate
  private LocalDateTime createdDate;

  public static Alarm fromDto(AlarmDto dto) {
    return Alarm.builder()
        .memberId(dto.getMemberId())
        .type(dto.getType())
        .contentId(dto.getContentId())
        .checkedOut(dto.isCheckedOut())
        .build();
  }

  public void checkOut() {
    this.checkedOut = true;

  }

}

