package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class ExistingResultException extends CustomException {

    private final ErrorCode errorCode;

    public ExistingResultException() {
        this.errorCode = ErrorCode.ALREADY_RESULT_EXISTS;
    }

}
