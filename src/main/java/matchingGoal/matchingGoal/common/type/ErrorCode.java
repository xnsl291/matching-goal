package matchingGoal.matchingGoal.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //common
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST,"RequestBody가 올바르지 않습니다."),

    //auth

    // member
    MEMBER_NOT_EXISTS(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다"),
    ALREADY_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "이미 등록된 이메일 입니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST,"비밀번호의 형식이 올바르지 않습니다."),


    ;
    private final HttpStatus status;
    private final String description;
}
