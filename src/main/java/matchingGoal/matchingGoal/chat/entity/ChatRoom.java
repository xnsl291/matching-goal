package matchingGoal.matchingGoal.chat.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matchingGoal.matchingGoal.chat.dto.CreateChatRoomRequestDto;
import matchingGoal.matchingGoal.member.model.entity.Member;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

  @Id
  @Setter
  private String id;
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @Default
  @JoinTable(name = "chatRoomMembers",
      joinColumns = @JoinColumn(name = "chatRoom"),
      inverseJoinColumns = @JoinColumn(name = "member"))
  private Set<Member> chatRoomMembers = new HashSet<>();

  private long matchingBoardId;
  private int closedYn;
  @CreatedDate
  private LocalDateTime createdDate;

  public static ChatRoom create(CreateChatRoomRequestDto request, Set<Member> members) {


    return ChatRoom.builder()
        .id(UUID.randomUUID().toString())
        .chatRoomMembers(members)
        .matchingBoardId(request.getMatchingBoardId())
        .closedYn(0)
        .build();
  }
  public void addMembers(Set<Member> members) {

    this.chatRoomMembers.addAll(members);

  }

  public void quit(long memberId) {

    for (Member member : this.getChatRoomMembers()) {
      if (member.getId() == memberId) {
        this.chatRoomMembers.remove(member);
      }
    }
  }

  public void close() {
    this.closedYn = 1;
  }

}
