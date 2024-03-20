package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class CancelTimeException extends CustomException {

    private final ErrorCode errorCode;

    public CancelTimeException() {
        this.errorCode = ErrorCode.CANCELLATION_TIME_OUT;
    }

}
