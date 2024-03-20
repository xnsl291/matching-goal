package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class AcceptedResultException extends CustomException {

    private final ErrorCode errorCode;

    public AcceptedResultException() {
        this.errorCode = ErrorCode.ALREADY_ACCEPTED_RESULT;
    }

}
