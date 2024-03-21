package matchingGoal.matchingGoal.alarm.service;


import java.util.List;
import matchingGoal.matchingGoal.alarm.domain.AlarmType;
import matchingGoal.matchingGoal.alarm.dto.AlarmDto;
import matchingGoal.matchingGoal.alarm.repository.AlarmRepository;
import matchingGoal.matchingGoal.chat.entity.dto.ChatMessageDto;
import matchingGoal.matchingGoal.member.dto.SignInDto;
import matchingGoal.matchingGoal.member.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class AlarmServiceTest  {
  @Autowired
  AlarmService alarmService;
  @Autowired
  AlarmRepository alarmRepository;
  @Autowired
  AuthService authService;

  @Test
  @DisplayName("create")
  @Transactional
  void createAlarm() {
    //given
    String token = authService.signIn(SignInDto.builder().email("test1@test.com").password("test123!@#").build()).getAccessToken();
    AlarmDto dto = AlarmDto.builder()
        .memberId(1)
        .type(AlarmType.CHAT)
        .contentId("1")
        .checkedOut(0)
        .build();
    //when
    alarmService.createAlarm("bearer " + token, dto);

    //then
    assert(alarmService.getAlarm("bearer " + token).size() == 1);

  }
  @Test
  @DisplayName("checkout")
//  @Transactional
  void checkout() {
    String token = authService.signIn(SignInDto.builder().email("test1@test.com").password("test123!@#").build()).getAccessToken();
    AlarmDto dto = AlarmDto.builder()
        .memberId(1)
        .type(AlarmType.CHAT)
        .contentId("1")
        .checkedOut(0)
        .build();
    token = "bearer " + token;
    alarmService.createAlarm(token, dto);
    List<AlarmDto> alarmList = alarmService.getAlarm(token);
    long id = alarmList.get(0).getId();
    //when
    alarmService.checkOut(token, id);
    //then
    assert(alarmService.getAlarmEntity(id).getCheckedOut() == 1);
  }

  @Test
  @DisplayName("messageAlarm")
  void messageAlarm() {
    //given
    String token = authService.signIn(SignInDto.builder().email("test1@test.com").password("test123!@#").build()).getAccessToken();

    ChatMessageDto messageDto = ChatMessageDto.builder()
        .message("ㅇㅇ")
        .chatRoomId("eac043e7-40e8-4798-bc2f-606bfb627b01")
        .receiverId(2)
        .build();
    //when
    alarmService.messageAlarm(messageDto);
    //then
    assert(alarmRepository.findAllByMemberId(2).size() == 1);


  }
}
