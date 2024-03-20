package matchingGoal.matchingGoal.matching.repository;

import matchingGoal.matchingGoal.matching.domain.entity.Comment;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  boolean existsByGame(Game game);
}
