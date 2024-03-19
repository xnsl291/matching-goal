package matchingGoal.matchingGoal.alarm.repository;

import java.util.List;
import matchingGoal.matchingGoal.alarm.domain.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
  List<Alarm> findAllByMemberId(long memberId);
}
