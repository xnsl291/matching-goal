package matchingGoal.matchingGoal.member.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class UnmatchedPasswordException extends CustomException {
    private final ErrorCode errorCode;

    public UnmatchedPasswordException() {
        this.errorCode = ErrorCode.WRONG_PASSWORD;
    }
}
