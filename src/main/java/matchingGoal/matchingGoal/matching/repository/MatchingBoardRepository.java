package matchingGoal.matchingGoal.matching.repository;

import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingBoardRepository extends JpaRepository<MatchingBoard, Long> {

}
