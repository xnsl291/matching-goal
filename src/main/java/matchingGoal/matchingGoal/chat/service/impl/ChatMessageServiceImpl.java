package matchingGoal.matchingGoal.chat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.chat.dto.ChatMessageDto;
import matchingGoal.matchingGoal.chat.dto.dtoConverter.ChatMessageConverter;
import matchingGoal.matchingGoal.chat.entity.ChatMessage;
import matchingGoal.matchingGoal.chat.repository.ChatMessageRepository;
import matchingGoal.matchingGoal.chat.service.ChatMessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
  private final ChatMessageRepository chatMessageRepository;
  private final ChatMessageConverter chatMessageConverter;
  private final RabbitTemplate rabbitTemplate;
  private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
  private final static String CHAT_QUEUE_NAME = "chat.queue";

  @Override
  @Transactional
  public ChatMessage createMessage(ChatMessageDto chatDto, String chatRoomId) {
    chatDto.setRoomId(chatRoomId);
    ChatMessage chatMessage = chatMessageConverter.toEntity(chatDto);
    chatMessageRepository.save(chatMessage);
    chatDto.setCreatedDate(chatMessage.getCreatedDate());
    rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chatDto);



    return chatMessage;
  }
}
