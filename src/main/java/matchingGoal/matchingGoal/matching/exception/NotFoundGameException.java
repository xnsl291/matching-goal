package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class NotFoundGameException extends CustomException {
  private final ErrorCode errorCode;

  public NotFoundGameException() {
    this.errorCode = ErrorCode.GAME_NOT_FOUND;
  }

}
