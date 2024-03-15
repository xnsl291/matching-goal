package matchingGoal.matchingGoal.matching.repository;

import java.util.List;
import java.util.Optional;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingBoardRepository extends JpaRepository<MatchingBoard, Long> {

  Optional<List<MatchingBoard>> findByMemberId(Long id);
}
