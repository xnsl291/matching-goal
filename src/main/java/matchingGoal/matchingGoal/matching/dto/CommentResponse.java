package matchingGoal.matchingGoal.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.matching.domain.entity.Comment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

  private Long commentId;
  private Long opponentId;
  private Long writerId;
  private Long writerImgId;
  private Long gameId;
  private Integer score;
  private String content;

  public static CommentResponse of(Comment comment) {
    return CommentResponse.builder()
        .commentId(comment.getId())
        .opponentId(comment.getOpponent().getId())
        .writerId(comment.getWriter().getId())
//        .writerImgId(comment.getWriter().getImageId())
        .gameId(comment.getGame().getId())
        .score(comment.getScore())
        .content(comment.getContent())
        .build();
  }
}