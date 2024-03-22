package matchingGoal.matchingGoal.chat.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.chat.dto.ChatMessageDto;
import matchingGoal.matchingGoal.chat.dto.ChatHistoryDto;
import matchingGoal.matchingGoal.chat.dto.ChatRoomMemberDto;
import matchingGoal.matchingGoal.chat.entity.ChatMessage;
import matchingGoal.matchingGoal.chat.entity.ChatRoom;
import matchingGoal.matchingGoal.chat.dto.ChatRoomListResponseDto;
import matchingGoal.matchingGoal.chat.repository.ChatMessageRepository;
import matchingGoal.matchingGoal.chat.repository.ChatRoomRepository;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final MemberRepository memberRepository;

  private final ChatMessageRepository chatMessageRepository;


  public String createChatRoom(long hostId, long guestId) {
    Member host = getMember(hostId);
    Member guest = getMember(guestId);
    List<Member> members = new ArrayList<>();
    members.add(host);
    members.add(guest);
    ChatRoom room = ChatRoom.create();
    chatRoomRepository.save(room);
    room.addMembers(members);
    log.info(room.getId());

    return room.getId();
  }

  public void addMembers(String chatRoomId, List<Member> members) {
    ChatRoom room = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
    room.addMembers(members);
    log.info(chatRoomId + " 에 " + members.toString() + " 추가");
  }

  public List<ChatRoomListResponseDto> myChat(long userId) {
    List<ChatRoom> myChat = chatRoomRepository.findListsByChatRoomMembersId(userId);

    return myChat.stream().map(ChatRoomListResponseDto::fromEntity).toList();
  }

  @Transactional
  public void quit(long userId, String chatRoomId) {
    ChatRoom chatRoom = getChatRoom(chatRoomId);
    chatRoom.quit(userId);
  }

  @Transactional
  public List<ChatMessageDto> getChatMessage(String chatRoomId) {
    List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomId(chatRoomId);
    for (ChatMessage chat : chatMessageList) {
      chat.changeReadYn(1);
    }

    return chatMessageList.stream()
        .map(ChatMessageDto::fromEntity).toList();
  }

  public ChatHistoryDto getChatHistory(String chatRoomId) {
    ChatRoom chatRoom = getChatRoom(chatRoomId);
    List<ChatRoomMemberDto> chatRoomMemberInfo = chatRoom.getChatRoomMembers().stream()
        .map(ChatRoomMemberDto::fromEntity).toList();
    List<ChatMessageDto> chatMessageList = getChatMessage(chatRoomId);

    return ChatHistoryDto.builder()
        .chatRoomMemberInfo(chatRoomMemberInfo)
        .chatMessageList(chatMessageList)
        .build();
  }

  public ChatRoom getChatRoom(String chatRoomId) {

    return chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
  }

  public Member getMember(long userId) {

    return memberRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
  }


}
