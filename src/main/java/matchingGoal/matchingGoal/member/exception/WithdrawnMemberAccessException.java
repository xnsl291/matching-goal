package matchingGoal.matchingGoal.member.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class WithdrawnMemberAccessException extends CustomException {
    private final ErrorCode errorCode;

    public WithdrawnMemberAccessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
