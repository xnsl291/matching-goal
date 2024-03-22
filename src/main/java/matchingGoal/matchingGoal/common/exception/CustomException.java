package matchingGoal.matchingGoal.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.common.type.ErrorCode;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomException extends RuntimeException {

    private ErrorCode errorCode;
    private String description;

    public CustomException (ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();

    }
}