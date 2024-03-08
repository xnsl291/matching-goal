package matchingGoal.matchingGoal.member.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class AlreadyRegisteredEmailException extends CustomException {
    private final ErrorCode errorCode;

    public AlreadyRegisteredEmailException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
