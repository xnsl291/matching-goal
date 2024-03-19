package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class AlreadyRequestException extends CustomException {

    private final ErrorCode errorCode;

    public AlreadyRequestException() {
        this.errorCode = ErrorCode.ALREADY_REQUEST_MATCHING;
    }

}
