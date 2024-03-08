package matchingGoal.matchingGoal.common.exception;

import lombok.Builder;
import lombok.Data;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode e){
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.builder()
                        .status(e.getStatus().value())
                        .error(e.getStatus().name())
                        .code(e.name())
                        .message(e.getDescription())
                        .build()
                );
    }
}