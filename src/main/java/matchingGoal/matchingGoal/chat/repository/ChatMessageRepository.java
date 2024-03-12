package matchingGoal.matchingGoal.chat.repository;

import java.util.List;
import java.util.Optional;
import matchingGoal.matchingGoal.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  Optional<ChatMessage> findById(long messageId);
  List<ChatMessage> findByChatRoomId(String chatRoomId);
}
