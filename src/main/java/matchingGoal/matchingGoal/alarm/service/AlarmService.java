package matchingGoal.matchingGoal.alarm.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.alarm.domain.AlarmType;
import matchingGoal.matchingGoal.alarm.dto.AlarmDto;
import matchingGoal.matchingGoal.alarm.domain.entity.Alarm;
import matchingGoal.matchingGoal.alarm.repository.AlarmRepository;
import matchingGoal.matchingGoal.chat.dto.ChatMessageDto;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import matchingGoal.matchingGoal.common.config.RabbitConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService {
  private final AlarmRepository alarmRepository;
  private final JwtTokenProvider jwtTokenProvider;

  private static final String ALARM_QUEUE_NAME = RabbitConfig.ALARM_QUEUE_NAME;
  private static final String ALARM_EXCHANGE = RabbitConfig.ALARM_EXCHANGE;
  private static final String ALARM_ROUTING_KEY = RabbitConfig.ALARM_ROUTING_KEY;

  //알람생성
  public long createAlarm(String token, AlarmDto dto) {

    return createAlarm(getMemberIdFromToken(token), dto.getType(), dto.getContentId());
  }

  public long createAlarm(long memberId, AlarmType type, String contentId) {
    Alarm alarm = Alarm.builder()
        .memberId(memberId)
        .type(type)
        .contentId(contentId)
        .build();
    alarmRepository.save(alarm);
    log.info("alarm for member: " + alarm.getMemberId() + " saved!");

    return alarm.getId();
  }

  //채팅메세지 알람
  @Transactional
  public void messageAlarm(ChatMessageDto dto) {
    Optional<Alarm> optionalAlarm = alarmRepository.findByMemberIdAndContentId(dto.getReceiverId(), dto.getChatRoomId());
    if (optionalAlarm.isPresent()) {
      Alarm alarm = optionalAlarm.get();
      alarm.messageAlarmUpdate();
      log.info("alarm update for member: " + alarm.getMemberId());
    } else {
      createAlarm(dto.getReceiverId(), AlarmType.CHAT, dto.getChatRoomId());
    }
  }

  //알람리스트 조회
  public List<AlarmDto> getAlarm(String token) {
    long memberId = getMemberIdFromToken(token);

    return alarmRepository.findAllByMemberId(memberId).stream().map(AlarmDto::fromEntity).toList();
  }



  //수신확인
  @Transactional
  public void checkOut(String token, long alarmId) {
    long memberId = getMemberIdFromToken(token);
    Alarm alarm = getAlarmEntity(alarmId);
    if (alarm.getMemberId() != memberId) {
      throw new RuntimeException();
    }
    alarm.checkOut();
  }

  public Alarm getAlarmEntity(long alarmId) {

    return alarmRepository.findById(alarmId).orElseThrow(RuntimeException::new);
  }

  private long getMemberIdFromToken(String token) {
    token = token.substring(7);

    return jwtTokenProvider.getId(token);
  }


}
