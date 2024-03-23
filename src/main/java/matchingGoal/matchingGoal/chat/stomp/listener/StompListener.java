package matchingGoal.matchingGoal.chat.stomp.listener;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.service.RedisService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompListener {

  private final RedisService redisService;

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectEvent event) {

    StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headers.getSessionId();
    String chatRoomId = Objects.requireNonNull(headers.getNativeHeader("chatRoomId")).get(0);
    long memberId = Long.parseLong(
        Objects.requireNonNull(headers.getNativeHeader("memberId")).get(0));

    redisService.addChatRoomMember(chatRoomId, sessionId, memberId);
    log.info("[connected] chatroomId: " + chatRoomId + " sessionId: " + sessionId + "  memberId: "
        + memberId);
  }

  @EventListener
  public void webSocketDisconnectListener(SessionDisconnectEvent event) {

    StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headers.getSessionId();

    redisService.removeChatRoomMember(sessionId);
    log.info("[disconnected] sessionId:" + sessionId);
  }


}
