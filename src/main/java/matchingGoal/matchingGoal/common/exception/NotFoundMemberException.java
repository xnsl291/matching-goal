package matchingGoal.matchingGoal.common.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class NotFoundMemberException extends RuntimeException {

  private final ErrorCode errorCode;

  public NotFoundMemberException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
  }

}
