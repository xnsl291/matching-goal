package matchingGoal.matchingGoal.matching.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  Optional<Game> findByBoardId_Id(Long boardId);

  List<Game> findByTeam1AndDateBetween(Member member, LocalDate startDate, LocalDate endDate);
  List<Game> findByTeam2AndDateBetween(Member member, LocalDate startDate, LocalDate endDate);


}
