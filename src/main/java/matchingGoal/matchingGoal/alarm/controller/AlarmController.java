package matchingGoal.matchingGoal.alarm.controller;

import matchingGoal.matchingGoal.alarm.dto.AlarmDto;
import matchingGoal.matchingGoal.common.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class AlarmController {

  @RabbitListener(queues = RabbitConfig.ALARM_QUEUE_NAME)
  public void receiveAlarmMessage(AlarmDto alarmDto) {
    System.out.println("received alarm message: " + alarmDto.getMessage());
  }
}
