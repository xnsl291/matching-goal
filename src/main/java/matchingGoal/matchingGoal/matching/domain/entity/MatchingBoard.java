package matchingGoal.matchingGoal.matching.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.dto.UpdateBoardRequestDto;
import matchingGoal.matchingGoal.member.model.entity.Member;

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

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member memberId;

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

  public void update(UpdateBoardRequestDto requestDto) {
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.modifiedDate = LocalDateTime.now();
  }
}
