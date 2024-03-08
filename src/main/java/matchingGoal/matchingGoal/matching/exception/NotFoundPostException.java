package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class NotFoundPostException extends CustomException {

  private final ErrorCode errorCode;

  public NotFoundPostException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

}
