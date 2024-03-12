package matchingGoal.matchingGoal.chat.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matchingGoal.matchingGoal.chat.entity.ChatMessage;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
  private String chatRoomId;
  private long memberId;
  private String message;
  private LocalDateTime createdDate;

  public static ChatMessageDto fromEntity(ChatMessage entity) {
    return ChatMessageDto.builder()
        .chatRoomId(entity.getChatRoomId())
        .memberId(entity.getMemberId())
        .message(entity.getMessage())
        .createdDate(entity.getCreatedDate())
        .build();
  }
}
