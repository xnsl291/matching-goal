package matchingGoal.matchingGoal.matching.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.image.model.entity.Image;
import matchingGoal.matchingGoal.image.repository.ImageRepository;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.domain.entity.Game;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingRequest;
import matchingGoal.matchingGoal.matching.dto.BoardRequestDto;
import matchingGoal.matchingGoal.matching.dto.BoardResponseDto;
import matchingGoal.matchingGoal.matching.dto.ListBoardDto;
import matchingGoal.matchingGoal.matching.dto.RequestMatchingDto;
import matchingGoal.matchingGoal.matching.dto.UpdateBoardDto;
import matchingGoal.matchingGoal.matching.exception.AlreadyRequestException;
import matchingGoal.matchingGoal.matching.exception.CompletedMatchingException;
import matchingGoal.matchingGoal.matching.exception.DeletedPostException;
import matchingGoal.matchingGoal.matching.exception.IllegalSearchTypeException;
import matchingGoal.matchingGoal.matching.exception.NotFoundMemberException;
import matchingGoal.matchingGoal.matching.exception.NotFoundPostException;
import matchingGoal.matchingGoal.matching.exception.NotFoundRequestException;
import matchingGoal.matchingGoal.matching.exception.SelfRequestException;
import matchingGoal.matchingGoal.matching.repository.GameRepository;
import matchingGoal.matchingGoal.matching.repository.MatchingBoardRepository;
import matchingGoal.matchingGoal.matching.repository.MatchingRequestRepository;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingService {

  private final MatchingBoardRepository boardRepository;
  private final GameRepository gameRepository;
  private final MemberRepository memberRepository;
  private final MatchingRequestRepository requestRepository;
  private final ImageRepository imageRepository;

  /**
   * 게시글 작성
   * @param requestDto - 게시글 작성 dto
   * @return 게시글 조회 dto
   */
  public BoardResponseDto createBoard(BoardRequestDto requestDto) {
    Member member = memberRepository.findById(requestDto.getMemberId())
        .orElseThrow(() -> new NotFoundMemberException(ErrorCode.MEMBER_NOT_FOUND));

    List<Image> images = findImagesById(requestDto.getImgList());

    MatchingBoard matchingBoard = MatchingBoard.builder()
        .member(member)
        .region(requestDto.getRegion())
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .status(StatusType.OPEN)
        .createdDate(LocalDateTime.now())
        .isDeleted(false)
        .viewCount(0)
        .imgList(images)
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

  public List<ListBoardDto> getBoardList(
      Integer page, String keyword, String type, String sort, String sortDirection, String date, String time) {

    // 페이지번호
    int pageNum = (page != null && page >= 0) ? page : 0;

    // 정렬 방향
    Sort.Direction direction = Direction.DESC;
    if (sortDirection.equals("asc")) {
      direction = Direction.ASC;
    }

    // 정렬 기준
    String sortType = "createdDate";
    switch (sort) {
      case "time":
        break;
      case "view":
        sortType = "viewCount";
        break;
    }

    // 페이징
    Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(direction, sortType));

    LocalDate parsedDate = (date != null) ? LocalDate.parse(date) : null;
    LocalTime parsedTime = (time != null) ? LocalTime.parse(time) : null;

    Page<ListBoardDto> boardPage;

    if (keyword != null && !keyword.isEmpty()) {
      switch (type) {
        case "title":
          boardPage = boardRepository.findByTitleContaining(keyword, pageable).map(this::convertToDto);
          break;
        case "writer":
          List<Member> members = memberRepository.findByNicknameContaining(keyword);
          List<Long> memberIds = members.stream().map(Member::getId).collect(Collectors.toList());
          boardPage = boardRepository.findByMemberIdIn(memberIds, pageable).map(this::convertToDto);
          break;
        case "region":
          boardPage = boardRepository.findByRegionContaining(keyword, pageable).map(this::convertToDto);
          break;
        default:
          throw new IllegalSearchTypeException(ErrorCode.ILLEGAL_SEARCH_TYPE);
      }
    } else {
      boardPage = boardRepository.findAll(pageable).map(this::convertToDto);
    }

    return boardPage.toList();
  }

  private ListBoardDto convertToDto(MatchingBoard matchingBoard) {
    Member member = matchingBoard.getMember();
    Game game = matchingBoard.getGame();

    return ListBoardDto.builder()
        .id(matchingBoard.getId())
        .memberId(member.getId())
        .memberImg(member.getImageId())
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
   * @param id - 게시글 id
   * @return 게시글 조회 dto
   */
  public BoardResponseDto getBoardById(Long id) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));

    boardRepository.increaseViewCountById(id);

    if (matchingBoard.getIsDeleted()) {
      throw new DeletedPostException(ErrorCode.DELETED_POST);
    }

    BoardResponseDto boardResponseDto = BoardResponseDto.of(matchingBoard);

    return boardResponseDto;
  }

  /**
   * 게시글 수정
   * @param id - 게시글 id
   * @param requestDto - 게시글 수정 dto
   * @return 게시글 조회 dto
   */
  public BoardResponseDto updateBoard(Long id, UpdateBoardDto requestDto) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));

    if (matchingBoard.getIsDeleted()) {
      throw new DeletedPostException(ErrorCode.DELETED_POST);
    }

    List<Image> images = findImagesById(requestDto.getImgList());
    matchingBoard.updateImg(images);
    matchingBoard.update(requestDto);
    boardRepository.save(matchingBoard);

    return getBoardById(matchingBoard.getId());
  }

  /**
   * 게시글 삭제
   * @param id - 게시글 id
   * @return 게시글 id
   */
  public Long deleteBoard(Long id) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));

    if (matchingBoard.getIsDeleted()) {
      throw new DeletedPostException(ErrorCode.DELETED_POST);
    }

    if (matchingBoard.getStatus() == StatusType.CLOSED) {
      throw new CompletedMatchingException(ErrorCode.ALREADY_COMPLETED_MATCHING);
    }

    List<MatchingRequest> requests = requestRepository.findByBoardId(id).orElse(Collections.emptyList());
    for (MatchingRequest request : requests) {
      request.refuse();
      requestRepository.save(request);
    }

    matchingBoard.delete();
    matchingBoard.getGame().delete();
    boardRepository.save(matchingBoard);

    return matchingBoard.getId();
  }

  /**
   * 경기 매칭 신청
   * @param id - 게시글 id
   * @param memberId - 신청자 id
   * @return "매칭 신청 완료"
   */
  public String requestMatching(Long id, Long memberId) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new NotFoundMemberException(ErrorCode.MEMBER_NOT_FOUND));

    if (matchingBoard.getIsDeleted()) {
      throw new DeletedPostException(ErrorCode.DELETED_POST);
    }

    if (matchingBoard.getStatus() == StatusType.CLOSED) {
      throw new CompletedMatchingException(ErrorCode.ALREADY_COMPLETED_MATCHING);
    }

    if (matchingBoard.getMember() == member) {
      throw new SelfRequestException(ErrorCode.SELF_REQUEST);
    }

    if (requestRepository.existsByBoardIdAndMemberId(id, memberId)) {
      throw new AlreadyRequestException(ErrorCode.ALREADY_REQUEST_MATCHING);
    }

    MatchingRequest matchingRequest = MatchingRequest.builder()
        .board(matchingBoard)
        .member(member)
        .createdDate(LocalDateTime.now())
        .build();
    requestRepository.save(matchingRequest);

    Member writer = matchingBoard.getMember();
    writer.setRequestCount((countRequestsByMember(writer)));
    memberRepository.save(writer);

    return "매칭 신청 완료";
  }

  /**
   * 매칭 신청한 팀 리스트 조회
   * @param id - 게시글 id
   * @return 매칭 신청 목록
   */
  public List<RequestMatchingDto> getRequestList(Long id) {
    List<MatchingRequest> requestList = requestRepository.findByBoardId(id)
        .orElse(Collections.emptyList());

    return requestList.stream().map(RequestMatchingDto::of)
        .sorted(Comparator.comparing(RequestMatchingDto::getCreatedDate))
        .collect(Collectors.toList());
  }

  /**
   * 매칭 신청 수락
   * @param id - 매칭 신청 id
   * @return "신청 수락 완료"
   */
  public String acceptRequest(Long id) {
    MatchingRequest request = requestRepository.findById(id)
        .orElseThrow(() -> new NotFoundRequestException(ErrorCode.REQUEST_NOT_FOUND));

    if (request.getBoard().getStatus() == StatusType.CLOSED) {
      throw new CompletedMatchingException(ErrorCode.ALREADY_COMPLETED_MATCHING);
    }

    request.accept();
    request.getBoard().acceptMatching();
    request.getBoard().getGame().setOpponent(request.getMember());
    requestRepository.save(request);

    List<MatchingRequest> otherRequests = requestRepository.findOtherRequestsByIdAndBoardId(id, request.getBoard().getId())
        .orElse(Collections.emptyList());
    for (MatchingRequest req : otherRequests) {
      req.refuse();
      requestRepository.save(req);
    }

    List<MatchingRequest> sameTimeRequests = findSameTimeRequests(id).orElse(Collections.emptyList());
    for (MatchingRequest req : sameTimeRequests) {
      req.refuse();
      requestRepository.save(req);
    }

    return "신청 수락 완료";
  }

  /**
   * 매칭 신청 거절
   * @param id - 매칭 신청 id
   * @return "신청 거절 완료"
   */
  public String refuseRequest(Long id) {
    MatchingRequest request = requestRepository.findById(id)
        .orElseThrow(() -> new NotFoundRequestException(ErrorCode.REQUEST_NOT_FOUND));

    if (request.getBoard().getStatus() == StatusType.CLOSED) {
      throw new CompletedMatchingException(ErrorCode.ALREADY_COMPLETED_MATCHING);
    }

    request.refuse();
    requestRepository.save(request);

    return "신청 거절 완료";
  }

  private Optional<List<MatchingRequest>> findSameTimeRequests(Long id) {
    MatchingRequest request = requestRepository.findById(id)
        .orElseThrow(() -> new NotFoundRequestException(ErrorCode.REQUEST_NOT_FOUND));

    Game game = request.getBoard().getGame();
    LocalDate date = game.getDate();
    LocalTime time = game.getTime();
    Long memberId = request.getMember().getId();

    return requestRepository.findSameTimeRequests(memberId, date, time, id);
  }

  private List<Image> findImagesById(List<Long> imgList) {
    List<Image> images = new ArrayList<>();

    if (imgList != null) {
      for (Long id : imgList) {
        Optional<Image> optionalImage = imageRepository.findById(id);
        optionalImage.ifPresent(images::add);
      }
    }

    return images;
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
