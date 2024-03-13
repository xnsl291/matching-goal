package matchingGoal.matchingGoal.member.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class InvalidTokenException extends CustomException {
    private final ErrorCode errorCode;

    public InvalidTokenException() {
        this.errorCode = ErrorCode.INVALID_TOKEN;
    }
}
