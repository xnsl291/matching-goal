package matchingGoal.matchingGoal.member.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class AlreadyRegisteredEmailException extends CustomException {
    private final ErrorCode errorCode;

    public AlreadyRegisteredEmailException() {
        this.errorCode = ErrorCode.ALREADY_REGISTERED_EMAIL;
    }
}
