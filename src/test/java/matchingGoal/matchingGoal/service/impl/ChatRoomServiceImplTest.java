package matchingGoal.matchingGoal.service.impl;



import java.util.ArrayList;
import java.util.List;
import matchingGoal.matchingGoal.chat.entity.ChatRoom;
import matchingGoal.matchingGoal.chat.entity.User;
import matchingGoal.matchingGoal.chat.dto.ChatRoomListResponse;
import matchingGoal.matchingGoal.chat.repository.ChatRoomRepository;
import matchingGoal.matchingGoal.chat.repository.UserRepository;
import matchingGoal.matchingGoal.chat.service.impl.ChatRoomServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChatRoomServiceImplTest {

  @Autowired
  ChatRoomRepository chatRoomRepository;
  @Autowired
  ChatRoomServiceImpl chatServiceimpl;
  @Autowired
  UserRepository userRepository;

  @Test
  @DisplayName("create")
  @Transactional
  public void createChatRoom() {
    //given

    //when
    String id = chatServiceimpl.createChatRoom(1, 2);

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
    String id = chatServiceimpl.createChatRoom(1, 2);

    //when
    List<User> users2 = new ArrayList<>();
    User third = userRepository.findById(3L).orElseThrow(RuntimeException::new);
    users2.add(third);
    chatServiceimpl.addMembers(id, users2);

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
    String id = chatServiceimpl.createChatRoom(1, 2);
    System.out.println(id);
    //when
    List<ChatRoomListResponse> result = chatServiceimpl.myChat(1L);

    boolean check = !result.isEmpty();
    //then
    assert check;

  }

  @Test
  @DisplayName("quit")
  @Transactional
  public void quit() {
    //given

    String id = chatServiceimpl.createChatRoom(1, 2);
    System.out.println(id);

    //when
    chatServiceimpl.quit(2, id);
    ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(RuntimeException::new);

    boolean result = chatRoom.getChatRoomMembers().size() == 1;

    System.out.println(chatRoom.getChatRoomMembers());
    //then

    assert result;

  }
}