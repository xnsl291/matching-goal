package matchingGoal.matchingGoal.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchStatisticResponse {
	private double winRate;
	private int totalMatches;
	private int wins;
	private int losses;
	private int draws;
}
