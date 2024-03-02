package matchingGoal.matchingGoal.config;

import matchingGoal.matchingGoal.chat.stomp.interceptor.StompHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/chat/inbox")
        .setAllowedOrigins("*")
        .addInterceptors(new StompHandshakeInterceptor())
        .withSockJS()
//        .setClientLibraryUrl()
        .setDisconnectDelay(10 * 1000);

  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes("/pub");
    registry.enableSimpleBroker("/sub");
  }
}
