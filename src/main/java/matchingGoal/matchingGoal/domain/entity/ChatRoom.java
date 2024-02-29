package matchingGoal.matchingGoal.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Builder
@Getter
@Setter
@DynamicUpdate
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

  @Id
  private long id;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "chatRoomMembers",
      joinColumns = @JoinColumn(name = "chatRoomId"),
      inverseJoinColumns = @JoinColumn(name = "userId"))
  private Set<User> chatRoomMembers = new HashSet<>();

  private LocalDateTime createdDate;

  public static ChatRoom create() {
    ChatRoom room = new ChatRoom();
    room.setId(Long.parseLong(UUID.randomUUID().toString()));

    return room;

  }

  public void addMembers(List<User> members) {

    this.chatRoomMembers.addAll(members);

  }




}
