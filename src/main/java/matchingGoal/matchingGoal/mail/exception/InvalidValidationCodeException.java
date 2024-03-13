package matchingGoal.matchingGoal.mail.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class InvalidValidationCodeException extends CustomException {
    private final ErrorCode errorCode;

    public InvalidValidationCodeException() {
        this.errorCode = ErrorCode.INVALID_CODE;
    }
}
