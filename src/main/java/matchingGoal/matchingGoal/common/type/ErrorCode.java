package matchingGoal.matchingGoal.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {

    //common
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST,"입력된 값이 올바르지 않습니다"),

    //auth
    INVALID_CODE(HttpStatus.BAD_REQUEST,"인증코드가 일치하지 않습니다"),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    INVALID_TOKEN(HttpStatus.NOT_FOUND, "토큰 정보를 찾을 수 없습니다"),
    WITHDRAWN_MEMBER(HttpStatus.BAD_REQUEST, "탈퇴한 사용자 입니다"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"만료된 토큰입니다"),

    // member
    MEMBER_NOT_EXISTS(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다"),
    IMAGE_NOT_EXISTS(HttpStatus.NOT_FOUND,"이미지를 찾을 수 없습니다"),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 등록된 이메일 입니다"),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST,"중복된 닉네임 입니다"),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST,"비밀번호의 형식이 올바르지 않습니다"),
    PASSWORD_NOT_UPDATED(HttpStatus.BAD_REQUEST, "새로운 비밀번호는 변경 전 비밀번호와 같을 수 없습니"),
    ALREADY_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, ""),

    // matching
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게임입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    SELF_REQUEST(HttpStatus.BAD_REQUEST,"모집글 작성자가 신청할 수 없습니다."),
    ALREADY_REQUEST_MATCHING(HttpStatus.BAD_REQUEST,"이미 신청한 모집글입니다."),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 신청건입니다."),
    ALREADY_COMPLETED_MATCHING(HttpStatus.BAD_REQUEST, "이미 매칭 완료된 게시글입니다."),
    DELETED_POST(HttpStatus.BAD_REQUEST, "삭제된 게시글입니다."),
    ILLEGAL_SEARCH_TYPE(HttpStatus.BAD_REQUEST, "잘못된 검색 타입입니다.");

    
    private final HttpStatus status;
    private final String description;
}
