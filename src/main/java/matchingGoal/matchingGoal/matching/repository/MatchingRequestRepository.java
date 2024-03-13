package matchingGoal.matchingGoal.matching.repository;

import java.util.List;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingRequest;
import matchingGoal.matchingGoal.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRequestRepository extends JpaRepository<MatchingRequest, Long> {

  boolean existsByBoardIdAndMemberId(Long id, Long id1);

  List<MatchingRequest> findByBoardId(Long id);
}
