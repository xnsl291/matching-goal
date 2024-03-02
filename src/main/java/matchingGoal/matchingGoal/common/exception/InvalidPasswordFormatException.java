package matchingGoal.matchingGoal.common.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class InvalidPasswordFormatException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidPasswordFormatException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

}
