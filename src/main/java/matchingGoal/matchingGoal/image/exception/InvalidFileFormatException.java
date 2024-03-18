package matchingGoal.matchingGoal.image.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class InvalidFileFormatException extends CustomException {
    private final ErrorCode errorCode;

    public InvalidFileFormatException() {
        this.errorCode = ErrorCode.IMAGE_NOT_EXISTS;
    }
}
