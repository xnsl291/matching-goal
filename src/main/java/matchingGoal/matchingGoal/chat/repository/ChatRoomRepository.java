package matchingGoal.matchingGoal.chat.repository;

import java.util.List;
import java.util.Optional;
import matchingGoal.matchingGoal.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  Optional<ChatRoom> findById(String chatRoomId);
  @Transactional(readOnly = true)
  List<ChatRoom> findListsByChatRoomMembersId(long userId);

}
