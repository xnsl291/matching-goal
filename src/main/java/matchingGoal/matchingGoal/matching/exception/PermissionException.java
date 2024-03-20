package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class PermissionException extends CustomException {

    private final ErrorCode errorCode;

    public PermissionException() {
        this.errorCode = ErrorCode.NO_PERMISSION;
    }

}
