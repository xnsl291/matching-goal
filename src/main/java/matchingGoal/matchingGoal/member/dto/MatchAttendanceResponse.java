package matchingGoal.matchingGoal.member.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchAttendanceResponse {
    private int totalMatches;
    private int cancel;
    private int noshow;
}
