package matchingGoal.matchingGoal.common.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
  public static final String CHAT_QUEUE_NAME = "chat.queue";
  public static final String CHAT_EXCHANGE_NAME = "chat.exchange";
  public static final String CHAT_ROUTING_KEY = "room.*";

  public static final String ALARM_QUEUE_NAME = "alarm.queue";
  public static final String ALARM_EXCHANGE = "alarm.exchange";
  public static final String ALARM_ROUTING_KEY = "memberId.*";

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

  //Queue 등록
  @Bean
  public Queue chatQueue() { return new Queue(CHAT_QUEUE_NAME, true);}

  //Exchange 등록
  @Bean
  public TopicExchange exchange() { return new TopicExchange(CHAT_EXCHANGE_NAME, true, false);}


  //Exchange, Queue 바인딩
  @Bean
  public Binding chatBinding(Queue chatQueue, TopicExchange exchange) {
    return BindingBuilder.bind(chatQueue).to(exchange).with(CHAT_ROUTING_KEY);
  }

  @Bean
  public Queue alarmQueue() { return new Queue(ALARM_QUEUE_NAME, true);}
  @Bean
  public TopicExchange alarmExchange() {
    return new TopicExchange(ALARM_EXCHANGE);
  }
  @Bean
  public Binding alarmBinding (Queue alarmQueue, TopicExchange exchange) {
    return BindingBuilder.bind(alarmQueue).to(exchange).with(ALARM_ROUTING_KEY);
  }

  @Bean
  SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
      ConnectionFactory connectionFactory) {
    final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(jsonMessageConverter());
    return factory;
  }

  @Bean
  public RabbitTemplate rabbitTemplate(){
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
    rabbitTemplate.setMessageConverter(jsonMessageConverter());
    rabbitTemplate.setRoutingKey(CHAT_ROUTING_KEY);
    return rabbitTemplate;
  }

  @Bean
  public ConnectionFactory connectionFactory(){
    CachingConnectionFactory factory = new CachingConnectionFactory();
    factory.setHost(rabbitHost);
    factory.setUsername(rabbitUser);
    factory.setPassword(rabbitPw);
    factory.setVirtualHost(rabbitVHost);
    factory.setPort(5672);
    return factory;
  }

  @Bean
  public Jackson2JsonMessageConverter jsonMessageConverter() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true);
    objectMapper.registerModule(dateTimeModule());

    return new Jackson2JsonMessageConverter(objectMapper);
  }
  @Bean
  public Module dateTimeModule() {
    return new JavaTimeModule();
}


}
