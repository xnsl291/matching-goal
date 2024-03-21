package matchingGoal.matchingGoal.member.dto;

import lombok.*;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.member.model.entity.Member;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponse {
    private Long gameId;
    private String opponentNickname;
    private LocalDate date;
    private LocalTime time;
    private String stadium;

    public static ScheduleResponse of(Game game, Member member) {
        Member opponent = game.getTeam1().getId().equals(member.getId()) ? game.getTeam2() : game.getTeam1();
        return ScheduleResponse.builder()
                .gameId(game.getId())
                .date(game.getDate())
                .time(game.getTime())
                .opponentNickname(opponent.getNickname())
                .stadium(game.getStadiumName())
                .build();
    }
}
