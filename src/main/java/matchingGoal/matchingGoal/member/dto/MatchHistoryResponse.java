package matchingGoal.matchingGoal.member.dto;

import lombok.*;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.Result;
import matchingGoal.matchingGoal.member.model.entity.Member;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchHistoryResponse {
    private Long resultId;
    private Long opponentId;
    private String opponentNickname;
    private String opponentImgUrl;
    private boolean isWin;
    private int score1;
    private int score2;
    private LocalDate date;
    private LocalTime time;

    public static MatchHistoryResponse of (Member member, Result result) {
        Game game = result.getGame();
        Member opponent = game.getTeam1().getId().equals(member.getId()) ? game.getTeam2() : game.getTeam1();

        boolean isMemberWin = result.getScore1() > result.getScore2() ;  // 글 작성자가 이겼는지?
        if (game.getTeam2().getId().equals(member.getId()) ) // 글 작성자 != 멤버 인지 확인
            isMemberWin = !isMemberWin ;

        return MatchHistoryResponse.builder()
                .resultId(result.getId())
                .opponentId(opponent.getId())
                .opponentImgUrl(opponent.getImageUrl())
                .opponentNickname(opponent.getNickname())
                .isWin(isMemberWin)
                .score1(result.getScore1())
                .score2(result.getScore2())
                .date(game.getDate())
                .time(game.getTime())
                .build();
    }
}
