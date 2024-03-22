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
  private String nickname;
  private String message;
  private long receiverId;
  private LocalDateTime createdDate;
  private int readYn;

  public static ChatMessageDto fromEntity(ChatMessage entity) {

    return ChatMessageDto.builder()
        .chatRoomId(entity.getChatRoomId())
        .memberId(entity.getMemberId())
        .nickname(entity.getNickname())
        .message(entity.getMessage())
        .createdDate(entity.getCreatedDate())
        .receiverId(entity.getReceiverId())
        .readYn(entity.getReadYn())
        .build();
  }
}
