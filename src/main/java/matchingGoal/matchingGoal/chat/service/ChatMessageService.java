package matchingGoal.matchingGoal.chat.service;

import matchingGoal.matchingGoal.chat.dto.ChatMessageDto;
import matchingGoal.matchingGoal.chat.entity.ChatMessage;
import org.springframework.stereotype.Service;

@Service
public interface ChatMessageService {

  ChatMessage createMessage(ChatMessageDto chatMessageDto, String chatRoomId);

}
