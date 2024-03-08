package matchingGoal.matchingGoal.chat.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.chat.entity.ChatRoom;
import matchingGoal.matchingGoal.chat.entity.User;
import matchingGoal.matchingGoal.chat.dto.ChatRoomListResponse;
import matchingGoal.matchingGoal.chat.dto.dtoConverter.ChatRoomConverter;
import matchingGoal.matchingGoal.chat.repository.ChatRoomRepository;
import matchingGoal.matchingGoal.chat.repository.UserRepository;
import matchingGoal.matchingGoal.chat.service.ChatRoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final UserRepository userRepository;
  private final ChatRoomConverter chatRoomConverter;


  @Override
  @Transactional
  public String createChatRoom(long hostId, long guestId) {
    User host = userRepository.findById(hostId).orElseThrow(RuntimeException::new);
    User guest = userRepository.findById(guestId).orElseThrow(RuntimeException::new);

    List<User> members = new ArrayList<>();

    members.add(host);
    members.add(guest);

    ChatRoom room = ChatRoom.create();

    room.addMembers(members);

    chatRoomRepository.save(room);

    log.info(room.getId());

    return room.getId();
  }

  @Override
  @Transactional
  public void addMembers(String chatRoomId, List<User> members) {
    ChatRoom room = chatRoomRepository.findById(chatRoomId).orElseThrow(RuntimeException::new);
    room.addMembers(members);

    log.info(chatRoomId + " 에 " + members.toString() +" 추가");
  }

  @Override
  public List<ChatRoomListResponse> myChat(long userId) {

    List<ChatRoom> myChat = chatRoomRepository.findListsByChatRoomMembersId(userId);

    return chatRoomConverter.toChatRoomListResponseList(myChat);
  }

  @Override
  public void quit(long userId, String chatRoomId) {

    ChatRoom chatRoom = getChatRoom(chatRoomId);
    chatRoom.quit(userId);

  }

  public ChatRoom getChatRoom(String chatRoomId) {

    return chatRoomRepository.findById(chatRoomId).orElseThrow(RuntimeException::new);

  }

  public User getUser(long userId) {

    return userRepository.findById(userId).orElseThrow(RuntimeException::new);
  }


}
