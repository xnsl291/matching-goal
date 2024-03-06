package matchingGoal.matchingGoal.matching.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingBoard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long memberId;  // todo: member 변경

//  private Long imgId;

  private String region;

  private Integer capacity;

  private String title;

  private String content;

  @Enumerated(EnumType.STRING)
  private StatusType status;

  private LocalDateTime createdDate;

  private Boolean isDeleted;

  private LocalDateTime deletedDate;

  private Long viewCount;

  private LocalDateTime modifiedDate;
}
