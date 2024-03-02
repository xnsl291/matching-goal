package matchingGoal.matchingGoal.common.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class AlreadyRegisteredEmailException extends RuntimeException {

    private final ErrorCode errorCode;

    public AlreadyRegisteredEmailException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

}
