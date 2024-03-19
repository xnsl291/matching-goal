package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class NotFoundMemberException extends CustomException {

  private final ErrorCode errorCode;

  public NotFoundMemberException() {
    this.errorCode = ErrorCode.MEMBER_NOT_FOUND;
  }

}
