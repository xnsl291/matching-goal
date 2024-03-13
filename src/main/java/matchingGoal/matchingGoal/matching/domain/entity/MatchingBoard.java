package matchingGoal.matchingGoal.matching.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.dto.UpdateBoardDto;
import matchingGoal.matchingGoal.member.model.entity.Member;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingBoard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

//  private Long imgId;

  private String region;

  private String title;

  private String content;

  @Enumerated(EnumType.STRING)
  private StatusType status;

  private LocalDateTime createdDate;

  private Boolean isDeleted;

  private LocalDateTime deletedDate;

  private Integer viewCount;

  private LocalDateTime modifiedDate;

  @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
  private Game game;

  public void update(UpdateBoardDto requestDto) {
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.modifiedDate = LocalDateTime.now();
  }

}
