package matchingGoal.matchingGoal.member.dto;

import lombok.Builder;
import lombok.Getter;
import matchingGoal.matchingGoal.matching.dto.CommentHistoryDto;
import java.util.List;

@Builder
@Getter
public class CommentHistoryResponse {
    private double totalRate;
    private List<CommentHistoryDto> comments;
}
