package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class ExistingCommentException extends CustomException {

    private final ErrorCode errorCode;

    public ExistingCommentException() {
        this.errorCode = ErrorCode.ALREADY_COMMENT_EXISTS;
    }

}
