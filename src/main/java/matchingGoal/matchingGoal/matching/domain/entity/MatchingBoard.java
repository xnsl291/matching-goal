package matchingGoal.matchingGoal.matching.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.image.model.entity.Image;
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

  @OneToMany
  @JoinColumn(name = "board_id")
  private List<Image> imgList;

  @OneToOne(mappedBy = "board")
  private Game game;

  @OneToMany(mappedBy = "board")
  private List<MatchingRequest> matchingRequest;

  public void update(UpdateBoardDto requestDto) {
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.modifiedDate = LocalDateTime.now();
  }

  public void updateImg(List<Image> images) {
    this.imgList = images;
  }

  public void delete() {
    this.isDeleted = true;
    this.deletedDate = LocalDateTime.now();
  }

  public void acceptMatching() {
    this.status = StatusType.매칭완료;
  }

  public void setGame(Game game) {
    this.game = game;
  }
}
