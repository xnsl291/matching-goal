package matchingGoal.matchingGoal.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ALREADY_REGISTERED_EMAIL("이미 등록된 이메일 입니다."),
    INVALID_PASSWORD_FORMAT("비밀번호의 형식이 올바르지 않습니다"),
    SELF_REQUEST("모집글 작성자가 신청할 수 없습니다."),
    ALREADY_REQUEST_MATCHING("이미 신청한 모집글입니다.");
    private final String description;
}
