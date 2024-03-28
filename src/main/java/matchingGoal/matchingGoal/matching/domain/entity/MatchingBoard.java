package matchingGoal.matchingGoal.matching.domain.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import lombok.Setter;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.dto.BoardUpdateDto;
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

  @Setter
  @Enumerated(EnumType.STRING)
  private StatusType status;

  private LocalDateTime createdDate;

  private boolean isDeleted;

  private LocalDateTime deletedDate;

  private int viewCount;

  private LocalDateTime modifiedDate;

  @ElementCollection
  @CollectionTable(name = "board_images",joinColumns = @JoinColumn(name = "board_id"))
  @Column(name = "image_url")
  private List<String> imageUrls;

  @Setter
  @OneToOne(mappedBy = "board")
  private Game game;

  @OneToMany(mappedBy = "board")
  private List<MatchingRequest> matchingRequest;

  public void update(BoardUpdateDto requestDto) {
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.imageUrls = requestDto.getImageUrls();
    this.modifiedDate = LocalDateTime.now();
  }

  public void delete() {
    this.isDeleted = true;
    this.deletedDate = LocalDateTime.now();
  }

}
