package matchingGoal.matchingGoal.image.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class NotFoundImageException extends CustomException {
    private final ErrorCode errorCode;

    public NotFoundImageException() {
        this.errorCode = ErrorCode.IMAGE_NOT_EXISTS;
    }
}
