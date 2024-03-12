package matchingGoal.matchingGoal.chat.dto.dtoConverter;


import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.chat.dto.ChatMessageDto;
import matchingGoal.matchingGoal.chat.entity.ChatMessage;
import matchingGoal.matchingGoal.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageConverter {

  private final ChatMessageRepository chatMessageRepository;

  public ChatMessage toEntity(ChatMessageDto dto) {

    return ChatMessage.builder()
        .message(dto.getMessage())
        .chatRoomId(dto.getChatRoomId())
        .memberId(dto.getMemberId())
        .build();
  }
}
