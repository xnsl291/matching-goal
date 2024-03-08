package matchingGoal.matchingGoal.chat.repository;

import matchingGoal.matchingGoal.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
