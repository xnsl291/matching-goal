package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class NotFoundRequestException extends CustomException {
  private final ErrorCode errorCode;

  public NotFoundRequestException() {
    this.errorCode = ErrorCode.REQUEST_NOT_FOUND;
  }

}
