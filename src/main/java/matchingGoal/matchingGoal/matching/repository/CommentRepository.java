package matchingGoal.matchingGoal.matching.repository;

import matchingGoal.matchingGoal.matching.domain.entity.Comment;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByOpponent(Member member);
  boolean existsByGame(Game game);
}

