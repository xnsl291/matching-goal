package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class DeletedPostException extends CustomException {

    private final ErrorCode errorCode;

    public DeletedPostException() {
        this.errorCode = ErrorCode.DELETED_POST;
    }

}
