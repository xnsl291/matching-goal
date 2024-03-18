package matchingGoal.matchingGoal.member.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class ExpiredTokenException extends CustomException {
    private final ErrorCode errorCode;

    public ExpiredTokenException() {
        this.errorCode = ErrorCode.EXPIRED_TOKEN;
    }
}
