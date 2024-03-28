package matchingGoal.matchingGoal.matching.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  List<Game> findByTeam1AndDateBetweenAndTeam2IsNotNull(Member member, LocalDate startDate, LocalDate endDate);
  List<Game> findByTeam2AndDateBetween(Member member, LocalDate startDate, LocalDate endDate);

  @Query(value =
      "SELECT g " +
          "FROM Game g " +
          "WHERE (g.team1 = :member OR g.team2 = :member) " +
          "AND (g.date < :today OR (g.date = :today AND g.time < :now)) " +
          "AND g.team2 IS NOT NULL " +
          "ORDER BY g.date DESC"
  )
  List<Game> findByMemberAndDateBeforeOrTodayAndTimeBeforeAndTeam2IsNotNullOrderByDateDesc(
      Member member,
      LocalDate today,
      LocalTime now
  );
}
