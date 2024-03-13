package matchingGoal.matchingGoal.matching.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matchingGoal.matchingGoal.member.model.entity.Member;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "board_id")
  private MatchingBoard board;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  private Boolean isAccepted;

  private LocalDateTime createdDate;

  public void accept() {
    this.isAccepted = true;
  }

}
