package matchingGoal.matchingGoal.service.impl;



import java.util.ArrayList;
import java.util.List;
import matchingGoal.matchingGoal.domain.entity.ChatRoom;
import matchingGoal.matchingGoal.domain.entity.User;
import matchingGoal.matchingGoal.dto.ChatRoomListResponse;
import matchingGoal.matchingGoal.repository.ChatRoomRepository;
import matchingGoal.matchingGoal.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChatServiceImplTest  {

  @Autowired
  ChatRoomRepository chatRoomRepository;
  @Autowired
  ChatServiceImpl chatServiceimpl;
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
}