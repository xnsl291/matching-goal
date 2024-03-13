package matchingGoal.matchingGoal.member.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class MemberNotFoundException extends CustomException {
    private final ErrorCode errorCode;

    public MemberNotFoundException() {
        this.errorCode = ErrorCode.MEMBER_NOT_EXISTS;
    }
}
