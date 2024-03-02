package matchingGoal.matchingGoal.chat.stomp.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class StompListener {

  @EventListener
  public void webSocketConnectListener(SessionConnectedEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();

    log.info("[connected] id : {}", sessionId);

  }

  @EventListener
  public void webSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();

    log.info("[disconnected] id : {}", sessionId);

  }


}
