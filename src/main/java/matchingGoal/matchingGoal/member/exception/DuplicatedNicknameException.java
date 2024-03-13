package matchingGoal.matchingGoal.member.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class DuplicatedNicknameException extends CustomException {
    private final ErrorCode errorCode;

    public DuplicatedNicknameException() {
        this.errorCode =ErrorCode.DUPLICATED_NICKNAME;
    }
}
