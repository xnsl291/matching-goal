package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class IllegalSearchTypeException extends CustomException {

  private final ErrorCode errorCode;

  public IllegalSearchTypeException() {
    this.errorCode = ErrorCode.ILLEGAL_SEARCH_TYPE;
  }

}
