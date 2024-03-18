package matchingGoal.matchingGoal.member.repository;

import java.util.List;
import matchingGoal.matchingGoal.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    List<Member> findByNicknameContaining(String keyword);
}
