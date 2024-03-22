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
import matchingGoal.matchingGoal.matching.domain.CancelType;
import matchingGoal.matchingGoal.member.model.entity.Member;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameCancel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "game_id")
  private Game game;

  @Setter
  private CancelType type;

  @Setter
  private Boolean isAgreed;

  private LocalDateTime createdDate;

}
