package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class SelfRequestException extends CustomException {

  private final ErrorCode errorCode;

  public SelfRequestException() {
    this.errorCode = ErrorCode.SELF_REQUEST;
  }

}
