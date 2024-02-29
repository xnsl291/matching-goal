package matchingGoal.matchingGoal.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.*;

@Component
@Slf4j

public class ChatHandler extends TextWebSocketHandler {
  private static List<WebSocketSession> list = new ArrayList<>();

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String payload = message.getPayload();
    log.info("payload: " + payload);
    for (WebSocketSession webSocketSession: list) {
      session.sendMessage(message);
    }

  }

}
