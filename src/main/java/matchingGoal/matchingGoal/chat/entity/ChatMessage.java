package matchingGoal.matchingGoal.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matchingGoal.matchingGoal.chat.dto.ChatMessageDto;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String chatRoomId;
  private long memberId;
  private String nickname;
  private String message;
  private long receiverId;
  @CreatedDate
  private LocalDateTime createdDate;
  private int readYn;

  public void changeReadYn(int yn) {
    this.readYn = yn;
  }

  public static ChatMessage fromDto(ChatMessageDto dto) {

    return ChatMessage.builder()
        .message(dto.getMessage())
        .chatRoomId(dto.getChatRoomId())
        .memberId(dto.getMemberId())
        .nickname(dto.getNickname())
        .receiverId(dto.getReceiverId())
        .readYn(dto.getReadYn())
        .build();
  }
}
