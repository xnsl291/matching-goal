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
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Alarm {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private long memberId;
  @Convert(converter = AlarmTypeAttributeConverter.class)
  private AlarmType type;
  private String contentId;
  @Default
  private int checkedOut = 0;
  @CreatedDate
  private LocalDateTime createdDate;

  public static Alarm fromDto(AlarmDto dto) {

    return Alarm.builder()
        .memberId(dto.getMemberId())
        .type(dto.getType())
        .contentId(dto.getContentId())
        .checkedOut(dto.getCheckedOut())
        .build();
  }

  public void checkOut() {

    this.checkedOut = 1;
  }
  public void messageAlarmUpdate() {

    this.checkedOut = 0;
    this.createdDate = LocalDateTime.now();
  }

}

