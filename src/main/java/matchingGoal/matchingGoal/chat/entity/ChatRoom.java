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
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

  @Id
  private String id;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @Default
  @JoinTable(name = "chatRoomMembers",
      joinColumns = @JoinColumn(name = "chatRoomId"),
      inverseJoinColumns = @JoinColumn(name = "userId"))
  private Set<User> chatRoomMembers = new HashSet<>();
  @CreatedDate
  private LocalDateTime createdDate;

  public static ChatRoom create() {
    ChatRoom room = new ChatRoom();
    room.setId(UUID.randomUUID().toString());

    return room;

  }
  public void addMembers(List<User> members) {

    this.chatRoomMembers.addAll(members);

  }
  //TODO:: 인원 0 됐을 때 처리 방법안 1) 즉시 방 폐쇄하고 삭제
  //                              2) 30일 딜레이 후에 삭제 (복구기능 가능성)
  public void quit(Long userId) {
    for (User user : this.getChatRoomMembers()) {

      if (user.getId() == userId) {
        this.chatRoomMembers.remove(user);

      }
    }
  }
}
