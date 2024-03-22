package matchingGoal.matchingGoal.chat.controller;

import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.chat.dto.ChatMessageDto;
import matchingGoal.matchingGoal.chat.service.ChatMessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

  private final static String CHAT_QUEUE_NAME = "chat.queue";
  private final ChatMessageService chatMessageService;

  @MessageMapping("chat.message.{chatRoomId}")
  public void send(@Payload ChatMessageDto chat, @DestinationVariable String chatRoomId) {

    chatMessageService.createMessage(chat, chatRoomId);

  }

  @RabbitListener(queues = CHAT_QUEUE_NAME)
  public void receive(ChatMessageDto chat) {

    chatMessageService.saveMessage(chat);

  }

}
