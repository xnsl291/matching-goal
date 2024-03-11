package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class AlreadyRequestException extends RuntimeException {

    private final ErrorCode errorCode;

    public AlreadyRequestException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

}
