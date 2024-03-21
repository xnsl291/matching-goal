package matchingGoal.matchingGoal.matching.exception;

import lombok.Getter;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
public class NonParticipatingException extends CustomException {
  private final ErrorCode errorCode;

  public NonParticipatingException() {
    this.errorCode = ErrorCode.NON_PARTICIPATING_TEAM;
  }

}
