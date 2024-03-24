package matchingGoal.matchingGoal.chat.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import matchingGoal.matchingGoal.chat.entity.ChatRoom;
import matchingGoal.matchingGoal.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  Optional<ChatRoom> findById(String chatRoomId);
  @Transactional(readOnly = true)
  List<ChatRoom> findListsByChatRoomMembersId(long userId);
  @Query("SELECT cr FROM ChatRoom cr JOIN cr.chatRoomMembers m WHERE cr.matchingBoardId = :matchingBoardId AND m IN :members")
  Optional<ChatRoom> findByMatchingBoardIdAndChatRoomMembers(@Param("matchingBoardId") long matchingBoardId, @Param("members") Set<Member> members);
  List<ChatRoom> findAllByMatchingBoardId(long matchingBoardId);

}
