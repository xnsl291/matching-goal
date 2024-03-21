package matchingGoal.matchingGoal.matching.repository;

import matchingGoal.matchingGoal.matching.domain.entity.GameCancel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameCancelRepository extends JpaRepository<GameCancel, Long> {

}
