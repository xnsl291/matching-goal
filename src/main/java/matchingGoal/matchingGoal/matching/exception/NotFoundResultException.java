package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class NotFoundResultException extends CustomException {
  private final ErrorCode errorCode;

  public NotFoundResultException() {
    this.errorCode = ErrorCode.RESULT_NOT_FOUND;
  }

}
