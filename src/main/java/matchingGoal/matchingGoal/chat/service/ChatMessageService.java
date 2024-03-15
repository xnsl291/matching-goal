package matchingGoal.matchingGoal.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.chat.dto.ChatMessageDto;
import matchingGoal.matchingGoal.chat.entity.ChatMessage;
import matchingGoal.matchingGoal.chat.repository.ChatMessageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {

  private final ChatMessageRepository chatMessageRepository;
  private final RabbitTemplate rabbitTemplate;
  private final static String CHAT_EXCHANGE_NAME = "chat.exchange";

  @Transactional
  public void createMessage(ChatMessageDto chatDto, String chatRoomId) {
    chatDto.setChatRoomId(chatRoomId);
    ChatMessage chatMessage = ChatMessage.fromDto(chatDto);
    chatMessageRepository.save(chatMessage);
    chatDto.setCreatedDate(chatMessage.getCreatedDate());
    rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chatDto);

  }
}
