package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class NotAvailableTimeException extends CustomException {

    private final ErrorCode errorCode;

    public NotAvailableTimeException() {
        this.errorCode = ErrorCode.NOT_AVAILABLE_TIME;
    }

}
