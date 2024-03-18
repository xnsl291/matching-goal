package matchingGoal.matchingGoal.matching.repository;

import java.util.List;
import java.util.Optional;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MatchingBoardRepository extends JpaRepository<MatchingBoard, Long> {

  Optional<List<MatchingBoard>> findByMemberId(Long id);

  @Modifying
  @Transactional
  @Query("UPDATE MatchingBoard mb SET mb.viewCount = mb.viewCount + 1 WHERE mb.id = :id")
  void increaseViewCountById(Long id);
}
