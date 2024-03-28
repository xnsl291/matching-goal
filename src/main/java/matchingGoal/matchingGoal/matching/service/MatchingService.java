package matchingGoal.matchingGoal.matching.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.alarm.domain.AlarmType;
import matchingGoal.matchingGoal.alarm.service.AlarmService;
import matchingGoal.matchingGoal.chat.service.ChatRoomService;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingRequest;
import matchingGoal.matchingGoal.matching.dto.BoardDto;
import matchingGoal.matchingGoal.matching.dto.BoardResponseDto;
import matchingGoal.matchingGoal.matching.dto.BoardUpdateDto;
import matchingGoal.matchingGoal.matching.dto.ListBoardDto;
import matchingGoal.matchingGoal.matching.dto.MatchingRequestResponseDto;
import matchingGoal.matchingGoal.matching.repository.GameRepository;
import matchingGoal.matchingGoal.matching.repository.MatchingBoardRepository;
import matchingGoal.matchingGoal.matching.repository.MatchingBoardSpecification;
import matchingGoal.matchingGoal.matching.repository.MatchingRequestRepository;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingService {

  private final MatchingBoardRepository boardRepository;
  private final GameRepository gameRepository;
  private final MatchingRequestRepository requestRepository;
  private final MemberService memberService;
  private final AlarmService alarmService;
  private final ChatRoomService chatRoomService;

  /**
   * 게시글 작성
   *
   * @param requestDto - 게시글 작성 dto
   * @return 게시글 조회 dto
   */
  public BoardResponseDto createBoard(String token, BoardDto requestDto) {
    Member member = memberService.getMemberInfo(token);

    MatchingBoard matchingBoard = MatchingBoard.builder()
        .member(member)
        .region(requestDto.getRegion())
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .status(StatusType.OPEN)
        .createdDate(LocalDateTime.now())
        .isDeleted(false)
        .viewCount(0)
        .imageUrls(requestDto.getImageUrls())
        .build();
    MatchingBoard savedBoard = boardRepository.save(matchingBoard);

    Game game = Game.builder()
        .board(savedBoard)
        .team1(member)
        .team2(null)
        .stadiumName(requestDto.getStadium())
        .date(LocalDate.parse(requestDto.getDate()))
        .time(LocalTime.parse(requestDto.getTime()))
        .isDeleted(false)
        .build();
    gameRepository.save(game);

    savedBoard.setGame(game);
    MatchingBoard board = boardRepository.save(savedBoard);

    return getBoardById(board.getId());
  }

  public Page<ListBoardDto> getBoardList(
      Integer page, int pageSize, String keyword, String type, String sort, String sortDirection,
      String date, String time
  ) {

    LocalDate parsedDate = (date != null) ? LocalDate.parse(date) : null;
    LocalTime parsedTime = (time != null) ? LocalTime.parse(time) : null;

    Pageable pageable = creatPageable(page, pageSize, sort, sortDirection);
    Page<MatchingBoard> boardPage = boardRepository.findAll(
        MatchingBoardSpecification.search(keyword, type, parsedDate, parsedTime), pageable);

    return boardPage.map(this::convertToListDto);
  }

  private Pageable creatPageable(Integer page, Integer pageSize, String sort,
      String sortDirection) {
    int pageNum = (page != null && page >= 0) ? page : 0;

    Sort.Direction direction = Direction.DESC;
    if (sortDirection.equals("asc")) {
      direction = Direction.ASC;
    }

    String sortType = "createdDate";
    switch (sort) {
      case "time":
        break;
      case "view":
        sortType = "viewCount";
        break;
    }

    return PageRequest.of(pageNum, pageSize, Sort.by(direction, sortType));
  }

  private ListBoardDto convertToListDto(MatchingBoard matchingBoard) {
    Member member = matchingBoard.getMember();
    Game game = matchingBoard.getGame();

    return ListBoardDto.builder()
        .id(matchingBoard.getId())
        .memberId(member.getId())
        .memberImgUrl(member.getImageUrl())
        .nickname(member.getNickname())
        .title(matchingBoard.getTitle())
        .createdDate(matchingBoard.getCreatedDate())
        .viewCount(matchingBoard.getViewCount())
        .status(matchingBoard.getStatus())
        .requestCount(matchingBoard.getMember().getRequestCount())
        .region(matchingBoard.getRegion())
        .stadium(game.getStadiumName())
        .date(game.getDate())
        .time(game.getTime())
        .build();
  }

  /**
   * 게시글 조회
   *
   * @param boardId - 게시글 id
   * @return 게시글 조회 dto
   */
  public BoardResponseDto getBoardById(Long boardId) {
    MatchingBoard matchingBoard = boardRepository.findById(boardId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    boardRepository.increaseViewCountById(boardId);

    if (matchingBoard.isDeleted()) {
      throw new CustomException(ErrorCode.DELETED_POST);
    }

    return BoardResponseDto.of(matchingBoard);
  }

  /**
   * 게시글 수정
   *
   * @param requestDto - 게시글 수정 dto
   * @return 게시글 조회 dto
   */
  @Transactional
  public BoardResponseDto updateBoard(String token, Long boardId, BoardUpdateDto requestDto) {
    MatchingBoard board = boardRepository.findById(boardId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    memberService.checkMemberPermission(token, board.getMember());

    if (board.isDeleted()) {
      throw new CustomException(ErrorCode.DELETED_POST);
    }

    if (board.getStatus() == StatusType.CLOSED) {
      throw new CustomException(ErrorCode.ALREADY_COMPLETED_MATCHING);
    }

    board.update(requestDto);

    return getBoardById(board.getId());
  }

  /**
   * 게시글 삭제
   *
   * @return 게시글 id
   */
  @Transactional
  public Long deleteBoard(String token, Long boardId) {
    MatchingBoard board = boardRepository.findById(boardId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    memberService.checkMemberPermission(token, board.getMember());

    if (board.isDeleted()) {
      throw new CustomException(ErrorCode.DELETED_POST);
    }

    if (board.getStatus() == StatusType.CLOSED) {
      throw new CustomException(ErrorCode.ALREADY_COMPLETED_MATCHING);
    }

    List<MatchingRequest> requests = requestRepository.findByBoardId(boardId)
        .orElse(Collections.emptyList());
    for (MatchingRequest request : requests) {
      request.setIsAccepted(false);
    }

    board.delete();
    board.getGame().setDeleted(true);

    chatRoomService.closeAllChatRoom(boardId);

    return board.getId();
  }

  /**
   * 경기 매칭 신청
   *
   * @return "매칭 신청 완료"
   */
  @Transactional
  public String requestMatching(String token, Long boardId) {
    MatchingBoard board = boardRepository.findById(boardId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    Member member = memberService.getMemberInfo(token);

    if (board.isDeleted()) {
      throw new CustomException(ErrorCode.DELETED_POST);
    }

    if (board.getStatus() == StatusType.CLOSED) {
      throw new CustomException(ErrorCode.ALREADY_COMPLETED_MATCHING);
    }

    if (board.getMember() == member) {
      throw new CustomException(ErrorCode.SELF_REQUEST);
    }

    if (requestRepository.existsByBoardIdAndMemberId(boardId, member.getId())) {
      throw new CustomException(ErrorCode.ALREADY_REQUEST_MATCHING);
    }

    MatchingRequest matchingRequest = MatchingRequest.builder()
        .board(board)
        .member(member)
        .createdDate(LocalDateTime.now())
        .build();
    requestRepository.save(matchingRequest);

    Member writer = board.getMember();
    writer.setRequestCount((countRequestsByMember(writer)));
    alarmService.createAlarm(writer.getId(), AlarmType.NEW_MATCHING_REQUEST,
        String.valueOf(boardId));

    return "매칭 신청 완료";
  }

  /**
   * 매칭 신청한 팀 리스트 조회
   *
   * @return 매칭 신청 목록
   */
  public List<MatchingRequestResponseDto> getRequestList(Long boardId) {
    List<MatchingRequest> requestList = requestRepository.findByBoardId(boardId)
        .orElse(Collections.emptyList());

    return requestList.stream()
        .map(MatchingRequestResponseDto::of)
        .sorted(Comparator.comparing(MatchingRequestResponseDto::getCreatedDate))
        .collect(Collectors.toList());
  }

  public List<MatchingRequestResponseDto> getRequestListByMember(Long memberId) {
    List<MatchingBoard> boardList = boardRepository.findByMemberIdAndStatus(memberId, StatusType.OPEN)
        .orElse(Collections.emptyList());

    return boardList.stream()
        .map(board -> getRequestList(board.getId()))
        .flatMap(List::stream)
        .sorted(Comparator.comparing(MatchingRequestResponseDto::getCreatedDate))
        .collect(Collectors.toList());
  }

  /**
   * 매칭 신청 수락
   *
   * @return "신청 수락 완료"
   */
  @Transactional
  public String acceptRequest(String token, Long requestId) {
    MatchingRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_FOUND));
    MatchingBoard matchingBoard = request.getBoard();
    long matchingBoardId = matchingBoard.getId();
    memberService.checkMemberPermission(token, request.getBoard().getMember());

    if (request.getBoard().getStatus() == StatusType.CLOSED) {
      throw new CustomException(ErrorCode.ALREADY_COMPLETED_MATCHING);
    }

    request.setIsAccepted(true);
    matchingBoard.setStatus(StatusType.CLOSED);
    matchingBoard.getGame().setTeam2(request.getMember());

    List<MatchingRequest> otherRequests = requestRepository.findOtherRequestsByIdAndBoardId(
            requestId, matchingBoardId)
        .orElse(Collections.emptyList());
    for (MatchingRequest req : otherRequests) {
      req.setIsAccepted(false);
    }

    List<MatchingRequest> sameTimeRequests = findSameTimeRequests(requestId).orElse(
        Collections.emptyList());
    for (MatchingRequest req : sameTimeRequests) {
      req.setIsAccepted(false);
    }
    alarmService.createAlarm(request.getMember().getId(), AlarmType.MATCHING_REQUEST_ACCEPTED,
        String.valueOf(matchingBoardId));
    chatRoomService.closeAllChatRoom(matchingBoardId);

    return "신청 수락 완료";
  }

  /**
   * 매칭 신청 거절
   *
   * @return "신청 거절 완료"
   */
  @Transactional
  public String refuseRequest(String token, Long requestId) {
    MatchingRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_FOUND));

    memberService.checkMemberPermission(token, request.getBoard().getMember());

    if (request.getBoard().getStatus() == StatusType.CLOSED) {
      throw new CustomException(ErrorCode.ALREADY_REQUEST_MATCHING);
    }
    alarmService.createAlarm(request.getMember().getId(), AlarmType.MATCHING_REQUEST_DENIED,
        String.valueOf(request.getBoard().getId()));
    request.setIsAccepted(false);

    return "신청 거절 완료";
  }

  private Optional<List<MatchingRequest>> findSameTimeRequests(Long requestId) {
    MatchingRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_FOUND));

    Game game = request.getBoard().getGame();
    LocalDate date = game.getDate();
    LocalTime time = game.getTime();
    Long memberId = request.getMember().getId();

    return requestRepository.findSameTimeRequests(memberId, date, time, requestId);
  }

  private Integer countRequestsByMember(Member member) {
    List<MatchingBoard> boards = boardRepository.findByMemberId(member.getId())
        .orElse(Collections.emptyList());

    int count = 0;
    for (MatchingBoard each : boards) {
      if (each.getMatchingRequest() != null) {
        count += each.getMatchingRequest().size();
      }
    }

    return count;
  }

}
