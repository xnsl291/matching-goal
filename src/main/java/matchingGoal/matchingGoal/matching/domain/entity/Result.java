package matchingGoal.matchingGoal.matching.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matchingGoal.matchingGoal.matching.dto.ResultDto;
import matchingGoal.matchingGoal.member.model.entity.Member;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Result {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "game_id")
  private Game game;

  private Integer score1;

  private Integer score2;

  private Integer duration;

  @Setter
  private Boolean isAccepted;

  public void update(ResultDto resultDto) {
    this.score1 = resultDto.getScore1();
    this.score2 = resultDto.getScore2();
    this.duration = resultDto.getDuration();
  }
}
