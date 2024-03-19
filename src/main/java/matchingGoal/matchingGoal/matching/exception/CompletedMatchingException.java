package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class CompletedMatchingException extends CustomException {

    private final ErrorCode errorCode;

    public CompletedMatchingException() {
        this.errorCode = ErrorCode.ALREADY_COMPLETED_MATCHING;
    }

}
