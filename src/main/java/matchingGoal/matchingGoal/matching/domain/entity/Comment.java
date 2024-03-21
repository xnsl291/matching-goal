package matchingGoal.matchingGoal.matching.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.member.model.entity.Member;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "opponent_id")
  private Member opponent;

  @ManyToOne
  @JoinColumn(name = "writer_id")
  private Member writer;

  @OneToOne
  @JoinColumn(name = "game_id")
  private Game game;

  private String content;

  private Integer score;

  private LocalDateTime createdDate;

  private LocalDateTime modifiedDate;

  private Boolean isDeleted;

  private LocalDateTime deletedDate;

}
