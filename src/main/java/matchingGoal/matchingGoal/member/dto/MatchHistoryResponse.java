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
    private int myScore;
    private int opponentScore;
    private LocalDate date;
    private LocalTime time;

    public static MatchHistoryResponse of (Member member, Result result) {
        Game game = result.getGame();
        Member opponent = game.getTeam1().getId().equals(member.getId()) ? game.getTeam2() : game.getTeam1();

        int myScore = 0, opponentScore = 0;
        if (member.getId().equals(game.getTeam1().getId())) {
            myScore = result.getScore1();
            opponentScore = result.getScore2();
        } else if (member.getId().equals(game.getTeam2().getId())){
            myScore = result.getScore2();
            opponentScore = result.getScore1();
        }

        boolean isMemberWin = myScore > opponentScore ;  // 글 작성자가 이겼는지?
//        if (game.getTeam2().getId().equals(member.getId()) ) // 글 작성자 != 멤버 인지 확인
//            isMemberWin = !isMemberWin ;

        return MatchHistoryResponse.builder()
                .resultId(result.getId())
                .opponentId(opponent.getId())
                .opponentImgUrl(opponent.getImageUrl())
                .opponentNickname(opponent.getNickname())
                .isWin(isMemberWin)
                .myScore(myScore)
                .opponentScore(opponentScore)
                .date(game.getDate())
                .time(game.getTime())
                .build();
    }
}
