package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class SelfRequestException extends RuntimeException {

  private final ErrorCode errorCode;

  public SelfRequestException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
  }

}
