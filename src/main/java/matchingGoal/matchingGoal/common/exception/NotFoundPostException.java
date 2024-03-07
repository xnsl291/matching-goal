package matchingGoal.matchingGoal.common.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class NotFoundPostException extends RuntimeException {

  private final ErrorCode errorCode;

  public NotFoundPostException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
  }

}
