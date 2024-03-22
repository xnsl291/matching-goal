package matchingGoal.matchingGoal.chat.stomp.listener;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.service.RedisService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompListener {

  private final RedisService redisService;

  @EventListener
  public void webSocketConnectListener(SessionConnectedEvent event)  {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();
    String chatRoomId = Objects.requireNonNull(headerAccessor.getFirstNativeHeader("chatRoomId"));
    long memberId = Long.parseLong(
        Objects.requireNonNull(headerAccessor.getFirstNativeHeader("memberId")));

    redisService.addChatRoomMember(chatRoomId, sessionId, memberId);

    log.info("[connected] chatroomId: " + chatRoomId + "sessionId: " + sessionId + "  memberId: " + memberId);
  }

  @EventListener
  public void webSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();
    String chatRoomId = headerAccessor.getFirstNativeHeader("chatRoomId");

    redisService.removeChatRoomMember(chatRoomId, sessionId);

    log.info("[connected]  chatroomId: " + chatRoomId + "sessionId: " + sessionId);
  }


}
