package matchingGoal.matchingGoal.chat.service;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.alarm.service.AlarmService;
import matchingGoal.matchingGoal.chat.dto.ChatMessageDto;
import matchingGoal.matchingGoal.chat.entity.ChatMessage;
import matchingGoal.matchingGoal.chat.repository.ChatMessageRepository;
import matchingGoal.matchingGoal.common.service.RedisService;
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
  private final RedisService redisService;
  private final AlarmService alarmService;

  @Transactional
  public void createMessage(ChatMessageDto chatDto, String chatRoomId) {
    chatDto.setChatRoomId(chatRoomId);

    chatDto.setCreatedDate(LocalDateTime.now());

    rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chatDto);

  }

  public void saveMessage(ChatMessageDto chatDto) {
    ChatMessage chatMessage = ChatMessage.fromDto(chatDto);
    Set<Object> count = redisService.getChatRoomMember(chatDto.getChatRoomId());
    if (count.size() >= 2) {
      chatDto.setReadYn(1);
    } else {
      chatDto.setReadYn(0);
      alarmService.messageAlarm(chatDto);
    }
    chatMessageRepository.save(chatMessage);
  }
}
