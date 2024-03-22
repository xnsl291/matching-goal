package matchingGoal.matchingGoal.alarm.domain;

public enum AlarmType {

  CHAT(0)
  , NEW_MATCHING_REQUEST(1)
  , MATCHING_REQUEST_DENIED(2)
  , MATCHING_REQUEST_ACCEPTED(3);
  private final Integer code;

  AlarmType(Integer code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
