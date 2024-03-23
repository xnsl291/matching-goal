package matchingGoal.matchingGoal.chat.service;



import java.util.ArrayList;
import java.util.List;
import matchingGoal.matchingGoal.chat.dto.CreateChatRoomRequestDto;
import matchingGoal.matchingGoal.chat.entity.ChatRoom;
import matchingGoal.matchingGoal.chat.dto.ChatRoomListResponseDto;
import matchingGoal.matchingGoal.chat.repository.ChatRoomRepository;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChatRoomServiceTest {

  @Autowired
  ChatRoomRepository chatRoomRepository;
  @Autowired
  ChatRoomService chatRoomService;
  @Autowired
  MemberRepository memberRepository;

  @Test
  @DisplayName("create")
  @Transactional
  public void createChatRoom() {
    //given
    CreateChatRoomRequestDto dto = CreateChatRoomRequestDto.builder()
                                    .matchingBoardId(1)
                                    .guestId(1)
                                    .build();
    //when
    String id = chatRoomService.createChatRoom(1, dto);

    //then
    ChatRoom newChat = chatRoomRepository.findById(id).orElseThrow(RuntimeException::new);

    boolean check = newChat.getChatRoomMembers().size() == 2;
    assert check;
  }

  @Test
  @DisplayName("addmembers")
  @Transactional
  public void addMembers() {
    //given
    CreateChatRoomRequestDto dto = CreateChatRoomRequestDto.builder()
        .matchingBoardId(1)
        .guestId(1)
        .build();
    String id = chatRoomService.createChatRoom(1, dto);

    //when
    List<Member> members2 = new ArrayList<>();
    Member third = memberRepository.findById(3L).orElseThrow(RuntimeException::new);
    members2.add(third);
    chatRoomService.addMembers(id, members2);

    //then
    ChatRoom newChat = chatRoomRepository.findById(id).orElseThrow(RuntimeException::new);

    boolean check = newChat.getChatRoomMembers().size() == 3;
    assert check;
  }

  @Test
  @DisplayName("mychat")
  @Transactional
  public void myChat() {

    //given
    CreateChatRoomRequestDto dto = CreateChatRoomRequestDto.builder()
        .matchingBoardId(1)
        .guestId(1)
        .build();
    String id = chatRoomService.createChatRoom(1, dto);

    //when
    List<ChatRoomListResponseDto> result = chatRoomService.myChat(1L);

    boolean check = !result.isEmpty();
    //then
    assert check;

  }

  @Test
  @DisplayName("quit")
  @Transactional
  public void quit() {
    //given
    CreateChatRoomRequestDto dto = CreateChatRoomRequestDto.builder()
        .matchingBoardId(1)
        .guestId(1)
        .build();
    String id = chatRoomService.createChatRoom(1, dto);

    //when

    chatRoomService.quit(2, id);
    ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(RuntimeException::new);

    boolean result = chatRoom.getChatRoomMembers().size() == 1;

    //then

    assert result;

  }
}