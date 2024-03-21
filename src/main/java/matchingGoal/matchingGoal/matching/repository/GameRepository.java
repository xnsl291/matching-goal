package matchingGoal.matchingGoal.matching.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  List<Game> findByTeam1AndDateBetween(Member member, LocalDate startDate, LocalDate endDate);
  List<Game> findByTeam2AndDateBetween(Member member, LocalDate startDate, LocalDate endDate);
  List<Game> findByTeam1OrTeam2AndDateLessThanEqualAndTimeLessThanOrderByDateDesc(Member team1, Member team1_copy , LocalDate date, LocalTime time);
}
