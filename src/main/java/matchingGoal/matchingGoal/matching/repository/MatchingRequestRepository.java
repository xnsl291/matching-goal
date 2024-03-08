package matchingGoal.matchingGoal.matching.repository;

import java.util.Optional;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingRequest;
import matchingGoal.matchingGoal.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRequestRepository extends JpaRepository<MatchingRequest, Long> {

  boolean findByBoardIdAndMemberId(MatchingBoard matchingBoard, Member member);

  boolean existsByBoardIdAndMemberId(MatchingBoard matchingBoard, Member member);
}
