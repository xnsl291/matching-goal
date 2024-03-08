package matchingGoal.matchingGoal.matching.repository;

import java.util.Optional;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  Optional<Game> findByBoardId_Id(Long boardId);

}
