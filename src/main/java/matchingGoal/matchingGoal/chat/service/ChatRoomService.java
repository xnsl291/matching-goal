package matchingGoal.matchingGoal.chat.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.chat.dto.ChatMessageDto;
import matchingGoal.matchingGoal.chat.entity.ChatRoom;
import matchingGoal.matchingGoal.chat.dto.ChatRoomListResponse;
import matchingGoal.matchingGoal.chat.repository.ChatMessageRepository;
import matchingGoal.matchingGoal.chat.repository.ChatRoomRepository;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService{

  private final ChatRoomRepository chatRoomRepository;
  private final MemberRepository memberRepository;

  private final ChatMessageRepository chatMessageRepository;


  @Transactional
  public String createChatRoom(long hostId, long guestId) {
    Member host = getMember(hostId);
    Member guest = getMember(guestId);

    List<Member> members = new ArrayList<>();

    members.add(host);
    members.add(guest);

    ChatRoom room = ChatRoom.create();

    room.addMembers(members);

    chatRoomRepository.save(room);

    log.info(room.getId());

    return room.getId();
  }

  @Transactional
  public void addMembers(String chatRoomId, List<Member> members) {
    ChatRoom room = chatRoomRepository.findById(chatRoomId).orElseThrow(RuntimeException::new);
    room.addMembers(members);

    log.info(chatRoomId + " 에 " + members.toString() + " 추가");
  }

  public List<ChatRoomListResponse> myChat(long userId) {

    List<ChatRoom> myChat = chatRoomRepository.findListsByChatRoomMembersId(userId);

    return myChat.stream().map(ChatRoomListResponse::fromEntity).toList();
  }

  public void quit(long userId, String chatRoomId) {

    ChatRoom chatRoom = getChatRoom(chatRoomId);
    chatRoom.quit(userId);

  }

  public List<ChatMessageDto> getChatMessage(String chatRoomId) {

    return chatMessageRepository.findByChatRoomId(chatRoomId).stream()
        .map(ChatMessageDto::fromEntity).toList();
  }

  public ChatRoom getChatRoom(String chatRoomId) {

    return chatRoomRepository.findById(chatRoomId).orElseThrow(RuntimeException::new);

  }

  public Member getMember(long userId) {

    return memberRepository.findById(userId).orElseThrow(RuntimeException::new);
  }


}