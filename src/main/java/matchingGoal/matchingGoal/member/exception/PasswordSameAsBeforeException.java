package matchingGoal.matchingGoal.member.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class PasswordSameAsBeforeException extends CustomException {
    private final ErrorCode errorCode;

    public PasswordSameAsBeforeException() {
        this.errorCode = ErrorCode.PASSWORD_NOT_UPDATED;
    }
}
