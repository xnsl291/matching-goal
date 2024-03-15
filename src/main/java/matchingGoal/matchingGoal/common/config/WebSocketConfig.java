package matchingGoal.matchingGoal.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  @Value("${spring.rabbitmq.username}")
  private String rabbitUser;
  @Value("${spring.rabbitmq.password}")
  private String rabbitPw;
  @Value("${spring.rabbitmq.host}")
  private String rabbitHost;
  @Value("${spring.rabbitmq.virtual-host}")
  private String rabbitVHost;
  @Value("${spring.rabbitmq.port}")
  private int rabbitPort;


  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
//        .setAllowedOrigins("*");
        .setAllowedOriginPatterns("*");
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry reg) {
    reg.enableStompBrokerRelay("/exchange")
        .setClientLogin(rabbitUser)
        .setClientPasscode(rabbitPw)
        .setSystemLogin(rabbitUser)
        .setSystemPasscode(rabbitPw)
        .setVirtualHost(rabbitVHost)
        .setRelayHost(rabbitHost)
        .setRelayPort(rabbitPort);
//    reg.enableSimpleBroker("/sub");
    reg.setPathMatcher(new AntPathMatcher("."));

    reg.setApplicationDestinationPrefixes("/pub");

  }
}
