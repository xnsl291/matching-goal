package matchingGoal.matchingGoal.matching.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  private int score1;

  private int score2;

  private int duration;

//  @Setter
//  private Boolean isAccepted;
//
//  public void update(ResultDto resultDto) {
//    this.score1 = resultDto.getScore1();
//    this.score2 = resultDto.getScore2();
//    this.duration = resultDto.getDuration();
//  }
}
