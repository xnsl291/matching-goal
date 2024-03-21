package matchingGoal.matchingGoal.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.matching.domain.entity.Comment;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentHistoryDto {
    private Long commentId;
    private Long gameId;

    private Long opponentId;
    private String opponentNickname;
    private String opponentImageUrl;

    private Integer rating;
    private String comment;
    private LocalDate date;
    private LocalTime time;

    public static CommentHistoryDto of(Comment comment) {
        return CommentHistoryDto.builder()
                .commentId(comment.getId())
                .gameId(comment.getGame().getId())
                .opponentId(comment.getOpponent().getId())
                .opponentNickname(comment.getOpponent().getNickname())
                .opponentImageUrl(comment.getOpponent().getImageUrl())
                .rating(comment.getScore())
                .comment(comment.getContent())
                .date(comment.getCreatedDate().toLocalDate())
                .time(comment.getCreatedDate().toLocalTime())
                .build();
    }
}