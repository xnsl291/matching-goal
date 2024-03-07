package matchingGoal.matchingGoal.common.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class NotFoundGameException extends RuntimeException {

  private final ErrorCode errorCode;

  public NotFoundGameException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
  }

}
