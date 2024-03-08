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
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST,"만료된 토큰입니다"),

    // member
    MEMBER_NOT_EXISTS(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다"),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 등록된 이메일 입니다"),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST,"중복된 닉네임 입니다"),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST,"비밀번호의 형식이 올바르지 않습니다"),


    ;
    private final HttpStatus status;
    private final String description;
}
