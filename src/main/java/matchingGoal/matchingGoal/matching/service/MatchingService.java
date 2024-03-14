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
import matchingGoal.matchingGoal.matching.dto.RequestMatchingDto;
import matchingGoal.matchingGoal.matching.dto.UpdateBoardDto;
import matchingGoal.matchingGoal.matching.exception.AlreadyRequestException;
import matchingGoal.matchingGoal.matching.exception.CompletedMatchingException;
import matchingGoal.matchingGoal.matching.exception.DeletedPostException;
import matchingGoal.matchingGoal.matching.exception.NotFoundMemberException;
import matchingGoal.matchingGoal.matching.exception.NotFoundPostException;
import matchingGoal.matchingGoal.matching.exception.NotFoundRequestException;
import matchingGoal.matchingGoal.matching.exception.SelfRequestException;
import matchingGoal.matchingGoal.matching.repository.GameRepository;
import matchingGoal.matchingGoal.matching.repository.MatchingBoardRepository;
import matchingGoal.matchingGoal.matching.repository.MatchingRequestRepository;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
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
   */
  public String createBoard(BoardRequestDto requestDto) {
    Member member = memberRepository.findById(requestDto.getMemberId())
        .orElseThrow(() -> new NotFoundMemberException(ErrorCode.MEMBER_NOT_FOUND));

    List<Image> images = findImagesById(requestDto.getImgList());

    MatchingBoard matchingBoard = MatchingBoard.builder()
        .member(member)
        .region(requestDto.getRegion())
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .status(StatusType.모집중)
        .createdDate(LocalDateTime.now())
        .isDeleted(false)
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

    return "게시글 등록 완료";
  }

  /**
   * 게시글 조회
   * @param id - 게시글 id
   * @return 게시글 dto
   */
  public BoardResponseDto getBoardById(Long id) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));

    if (matchingBoard.getIsDeleted()) {
      throw new DeletedPostException(ErrorCode.DELETED_POST);
    }

    return BoardResponseDto.of(matchingBoard);
  }

  /**
   * 게시글 수정
   * @param id - 게시글 id
   * @param requestDto - 게시글 수정 dto
   * @return "게시글 수정 완료"
   */
  public String updateBoard(Long id, UpdateBoardDto requestDto) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));

    if (matchingBoard.getIsDeleted()) {
      throw new DeletedPostException(ErrorCode.DELETED_POST);
    }

    List<Image> images = findImagesById(requestDto.getImgList());
    matchingBoard.updateImg(images);
    matchingBoard.update(requestDto);
    boardRepository.save(matchingBoard);

    return "게시글 수정 완료";
  }

  /**
   * 게시글 삭제
   * @param id - 게시글 id
   * @return "게시글 삭제 완료"
   */
  public String deleteBoard(Long id) {
    MatchingBoard matchingBoard = boardRepository.findById(id)
        .orElseThrow(() -> new NotFoundPostException(ErrorCode.POST_NOT_FOUND));

    if (matchingBoard.getIsDeleted()) {
      throw new DeletedPostException(ErrorCode.DELETED_POST);
    }

    if (matchingBoard.getStatus() == StatusType.매칭완료) {
      throw new CompletedMatchingException(ErrorCode.ALREADY_COMPLETED_MATCHING);
    }

    Optional<List<MatchingRequest>> optionalList = requestRepository.findByBoardId(id);
    if (optionalList.isPresent()) {
      List<MatchingRequest> requests = optionalList.get();
      for (MatchingRequest request : requests) {
        request.refuse();
        requestRepository.save(request);
      }
    }

    matchingBoard.delete();
    matchingBoard.getGame().delete();
    boardRepository.save(matchingBoard);

    return "게시글 삭제 완료";
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

    if (matchingBoard.getStatus() == StatusType.매칭완료) {
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

    if (request.getBoard().getStatus() == StatusType.매칭완료) {
      throw new CompletedMatchingException(ErrorCode.ALREADY_COMPLETED_MATCHING);
    }

    request.accept();
    request.getBoard().acceptMatching();
    request.getBoard().getGame().setOpponent(request.getMember());
    requestRepository.save(request);

    Optional<List<MatchingRequest>> optionalList1 = requestRepository.findOtherRequestsByIdAndBoardId(id, request.getBoard().getId());
    if (optionalList1.isPresent()) {
      List<MatchingRequest> otherRequests = optionalList1.get();
      for (MatchingRequest req : otherRequests) {
        req.refuse();
        requestRepository.save(req);
      }
    }

    Optional<List<MatchingRequest>> optionalList2 = findSameTimeRequests(id);
    if (optionalList2.isPresent()) {
      List<MatchingRequest> sameTimeRequests = optionalList2.get();
      for (MatchingRequest req : sameTimeRequests) {
        req.refuse();
        requestRepository.save(req);
      }
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

    if (request.getBoard().getStatus() == StatusType.매칭완료) {
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

}
