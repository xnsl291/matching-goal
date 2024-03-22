package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class NotFoundCancelException extends CustomException {
  private final ErrorCode errorCode;

  public NotFoundCancelException() {
    this.errorCode = ErrorCode.CANCEL_NOT_FOUND;
  }

}
