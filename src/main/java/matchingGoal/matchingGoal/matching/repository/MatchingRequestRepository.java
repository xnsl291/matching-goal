package matchingGoal.matchingGoal.matching.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRequestRepository extends JpaRepository<MatchingRequest, Long> {

  boolean existsByBoardIdAndMemberId(Long boardId, Long memberId);

  Optional<List<MatchingRequest>> findByBoardId(Long id);

  @Query("SELECT mr FROM MatchingRequest mr WHERE mr.id <> :id AND mr.board.id = :boardId")
  Optional<List<MatchingRequest>> findOtherRequestsByIdAndBoardId(Long id, Long boardId);

  @Query("SELECT mr FROM MatchingRequest mr WHERE mr.id <> :id AND mr.member.id = :memberId AND mr.board.game.date = :date AND mr.board.game.time = :time")
  Optional<List<MatchingRequest>> findSameTimeRequests(Long memberId, LocalDate date, LocalTime time, Long id);
}
