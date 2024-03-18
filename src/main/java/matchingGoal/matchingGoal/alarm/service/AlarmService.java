package matchingGoal.matchingGoal.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.alarm.dto.AlarmDto;
import matchingGoal.matchingGoal.alarm.entity.Alarm;
import matchingGoal.matchingGoal.alarm.repository.AlarmRepository;
import matchingGoal.matchingGoal.common.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService {
  private final RabbitTemplate rabbitTemplate;
  private final AlarmRepository alarmRepository;

  private static final String ALARM_QUEUE_NAME = RabbitConfig.ALARM_QUEUE_NAME;
  private static final String ALARM_EXCHANGE = RabbitConfig.ALARM_EXCHANGE;
  private static final String ALARM_ROUTING_KEY = RabbitConfig.ALARM_ROUTING_KEY;

  public void createAlarm(AlarmDto dto, long memberId) {
    Alarm alarm = Alarm.fromDto(dto);
    alarmRepository.save(alarm);
    AlarmDto newDto = AlarmDto.fromEntity(alarm);
    rabbitTemplate.convertAndSend( ALARM_EXCHANGE, ALARM_ROUTING_KEY + memberId, newDto);
  }

}
